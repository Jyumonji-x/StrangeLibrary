package se24.borrowservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se24.borrowservice.controller.request.BorrowRequest;
import se24.borrowservice.controller.request.ReserveRequest;
import se24.borrowservice.controller.request.ReturnRequest;
import se24.borrowservice.domain.*;
import se24.borrowservice.repository.BorrowRepository;
import se24.borrowservice.repository.RuleRepository;
import se24.borrowservice.tool.DateChange;
import se24.borrowservice.tool.ReturnMap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class BorrowService {
    private final RestTemplate restTemplate;
    private final BorrowRepository borrowRepository;
    private final RuleRepository ruleRepository;

    @Autowired
    public BorrowService(RestTemplate restTemplate, BorrowRepository borrowRepository, RuleRepository repository) {
        this.restTemplate = restTemplate;
        this.borrowRepository = borrowRepository;
        this.ruleRepository = repository;
    }

    public String getIdentityByUsername(String username) {
        String identity = "";
        try {
            // 接口调用失败和session找不到目标的结果都是返回null给admin赋值
            ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:9090/api/identity/" + username, String.class);
            identity = responseEntity.getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("获取identity接口调用失败，请检查user-service");
        }
        return identity;
    }

    public Integer getCreditByUsername(String username) {
        Integer credit = 0;
        try {
            // 接口调用失败和session找不到目标的结果都是返回null给admin赋值
            ResponseEntity<Integer> responseEntity = restTemplate.getForEntity("http://localhost:9090/api/credit/" + username, Integer.class);
            credit = responseEntity.getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("获取credit接口调用失败，请检查user-service");
        }
        return credit;
    }

    public User getUserBySession(String session) {
        User user = null;
        try {
            // 接口调用失败和session找不到目标的结果都是返回null给admin赋值
            ResponseEntity<User> responseEntity = restTemplate.postForEntity("http://localhost:9090/api/session/" + session, null, User.class);
            user = responseEntity.getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("检验session接口调用失败，请检查user-service");
        }
        return user;
    }

    public Copy getCopyByCopyId(String copyId) {
        Copy copy = null;
        try {
            // 接口调用失败和session找不到目标的结果都是返回null给admin赋值
            ResponseEntity<Copy> responseEntity = restTemplate.getForEntity("http://localhost:9091/api/book/copy/" + copyId, Copy.class);
            copy = responseEntity.getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("查询副本接口调用失败，请检查book-service");
        }
        return copy;
    }

    /**
     * borrow 借书接口
     */

    // 检测管理员和书本状态
    public ReturnMap borrow(BorrowRequest request) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        // 检测session管理员权限
        User admin;
        admin = getUserBySession(request.getSession());
        if (admin == null || !admin.isAdmin()) {
            map.setMessage("借书需要管理员权限");
            return map;
        }
        // 检测书本是否可借
        String copyId = request.getCopyId();
        if (borrowRepository.existsByCopyId(copyId)) {
            // 这本书正在外借,借不得了
            map.setMessage("只能借阅在库副本");
            return map;
        }
        return borrow2(request, admin);
    }

    // 检测副本信息和用户规则、借书人所在地和副本所在地
    private ReturnMap borrow2(BorrowRequest request, User admin) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        // 检测副本信息
        Copy copy;
        copy = getCopyByCopyId(request.getCopyId());
        if (copy == null || !copy.getStatus().equals("在库")) {
            System.out.println("copy并非在库状态" + copy);
            map.setMessage("只能借阅在库副本");
            return map;
        }
        // 获取用户规则
        Rule rule;
        String identity = getIdentityByUsername(request.getBorrower());
        rule = ruleRepository.findRuleByIdentity(identity);
        if (rule == null) {
            map.setMessage("读者用户名不存在或身份异常,请检查");
            return map;
        }
        // 检测最大借书数量、所在地匹配问题
        return borrow3(request, admin, copy, rule);
    }

    private ReturnMap borrow3(BorrowRequest request, User admin, Copy copy, Rule rule) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        // 检测最大借书数量
        int max = rule.getBorrowMaxNum();
        int num = borrowRepository.countByBorrower(request.getBorrower());
        if (num >= max) {
            map.setMessage("预约或借书数量已达上限");
            return map;
        }
        // 所在地匹配问题
        if (!request.getBranch().equals(copy.getBranch())) {
            map.setMessage(copy.getCopyId() + ":不能借阅其他分馆的副本");
            return map;
        }
        return borrow4(request, admin, copy, rule);
    }

    // 检查信用分
    private ReturnMap borrow4(BorrowRequest request, User admin, Copy copy, Rule rule) {
        Integer credit = getCreditByUsername(request.getBorrower());
        ReturnMap map = new ReturnMap();
        if (credit <= 0) {
            map.setRtn(0);
            map.setMessage("用户信用为0,支付罚单或申请恢复后才能借书");
            return map;
        }
        return doBorrow(request, admin, copy, rule);
    }

    private ReturnMap doBorrow(BorrowRequest request, User admin, Copy copy, Rule rule) {
        ReturnMap map = new ReturnMap();
        DateChange now = new DateChange(new Date());
        Date timeValid = now.getDateOfSecondsLater(rule.getBorrowValidTime());
        String borrower = request.getBorrower();
        // 修改副本状态
        copy.setStatus("借出");
        copy.setBorrower(borrower);
        try {
            restTemplate.put("http://localhost:9091/api/book/copy", copy);
            // 本地生成新的借出
            Borrow borrow = new Borrow();
            borrow.setCopyId(copy.getCopyId());
            borrow.setTitle(copy.getTitle());
            borrow.setStatus("借出");
            borrow.setBorrower(borrower);
            borrow.setTime(now.getDate());
            borrow.setValidTime(timeValid);
            borrowRepository.save(borrow);
            // 生成系统记录
            Log log = new Log();
            log.setTitle(copy.getTitle());
            log.setCopyId(copy.getCopyId());
            log.setUsername(borrower);
            log.setOperator(admin.getUsername());
            log.setBranch(request.getBranch());
            log.setCategory("现场借书");
            log.setPrice(0);
            log.setTime(now.getDate());
            log.setTimeValid(timeValid);
            restTemplate.put("http://localhost:9099/api/logger/log", log);
            map.setRtn(1);
            map.setMessage("借书成功");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("更新副本或系统记录接口调用失败，检查对应service");
            map.setRtn(0);
            map.setMessage("借书失败");
        }
        return map;
    }

    /**
     * retCopy 还书接口
     */

    public ReturnMap retCopy(ReturnRequest request) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        // 检测session管理员权限
        User admin;
        admin = getUserBySession(request.getSession());
        if (admin == null || !admin.isAdmin()) {
            map.setMessage("借书需要管理员权限");
            return map;
        }
        // 检测书本是否借出
        String copyId = request.getCopyId();
        if (!borrowRepository.existsByCopyIdAndStatus(copyId, "借出")) {
            // 这本书没外借，还不了
            map.setMessage("只能归还借出的副本");
            return map;
        }
        return retCopy2(request, admin);
    }

    // 检测还书的状态是否合法
    private ReturnMap retCopy2(ReturnRequest request, User admin) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        // 检测状态参数内容
        switch (request.getStatus()) {
            case "完好":
            case "损坏":
            case "遗失":
                break;
            default:
                map.setMessage("还书状态不符合要求，请检查");
                return map;
        }
        // 检测副本是否存在
        Copy copy;
        copy = getCopyByCopyId(request.getCopyId());
        if (copy == null) {
            System.out.println("copy不存在");
            map.setMessage("不能归还不存在的副本");
            return map;
        }
        return doRetCopy(request, admin, copy);
    }

    ReturnMap doRetCopy(ReturnRequest request, User admin, Copy copy) {
        ReturnMap map = new ReturnMap();
        String status = request.getStatus().equals("完好") ? "在库" : request.getStatus();
        String borrower = copy.getBorrower();
        DateChange now = new DateChange(new Date());
        // 改副本状态
        copy.setStatus(status);
        copy.setBorrower(null);
        copy.setBranch(request.getBranch());
        try {
            // 接口调用失败和session找不到目标的结果都是返回null给admin赋值
            restTemplate.put("http://localhost:9091/api/book/copy", copy);

            // 生成一条待评评论
            Comment comment = new Comment();
            comment.setUsername(borrower);
            comment.setIsbn(copy.getISBN());
            comment.setBookName(copy.getTitle());
            restTemplate.put("http://localhost:9093/api/comment/add", comment);

            // 生成罚款
            // 经过前面的检测，这里一定能获取到的
            double price = restTemplate.getForObject("http://localhost:9091/api/book/price/" + copy.getISBN(), Double.class);
            Borrow borrow = borrowRepository.findBorrowByCopyId(copy.getCopyId());
            boolean needFine = false;
            double finePrice = 0;
            String fineCategory = "还书";
            int creditChange = 0;
            switch (request.getStatus()) {
                case "完好":
                    if (now.getDate().after(borrow.getValidTime())) {
                        needFine = true;
                        finePrice = (price / 4);
                        fineCategory = "借阅逾期";
                        creditChange = -20;
                        map.setMessage("超出借阅期限，需缴纳罚款，信用积分-20");
                    }
                    break;
                case "损坏":
                    needFine = true;
                    finePrice = (price / 2);
                    fineCategory = "图书损坏";
                    creditChange = -30;
                    map.setMessage("图书损坏，需缴纳罚款，信用积分-30");
                    break;
                case "遗失":
                    needFine = true;
                    finePrice = price;
                    fineCategory = "图书遗失";
                    creditChange = -40;
                    map.setMessage("图书遗失，需缴纳罚款，信用积分-40");
                    break;
            }
            // 罚款小数点后2位向上取整
            BigDecimal priceBD = BigDecimal.valueOf(finePrice);
            finePrice = priceBD.setScale(2, RoundingMode.CEILING).doubleValue();

            // 还书记录
            Log log = new Log();
            log.setTitle(copy.getTitle());
            log.setCopyId(copy.getCopyId());
            log.setUsername(borrower);
            log.setOperator(admin.getUsername());
            log.setBranch(request.getBranch());
            log.setCategory(fineCategory);
            log.setPrice(finePrice);
            log.setTime(now.getDate());
            if (needFine) {
                log.setNote("信用分变化" + creditChange);
                // 产生罚单
                Fine fine = new Fine();
                fine.setCopyId(copy.getCopyId());
                fine.setPrice(finePrice);
                fine.setReason(fineCategory);
                fine.setUsername(borrower);
                fine.setBookName(copy.getTitle());
                restTemplate.put("http://localhost:9094/api/fine/add", fine);
                // 扣信用分
                restTemplate.put("http://localhost:9090/api/credit/" + borrower + "/" + creditChange, null);
            }
            restTemplate.put("http://localhost:9099/api/logger/log", log);

            // 还书之后本服务保存的Borrow就应该删除了
            borrowRepository.deleteBorrowByCopyId(request.getCopyId());
            map.setRtn(1);
            map.setMessage("还书成功");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("更新副本或系统记录接口调用失败，检查对应service");
            map.setRtn(0);
            map.setMessage("还书失败");
        }
        return map;
    }

    /**
     * reserve 预约接口
     */

    // 检测用户存在 检测副本状态
    public ReturnMap reserve(ReserveRequest request) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        User user;
        user = getUserBySession(request.getSession());
        if (user == null) {
            map.setMessage("需要登录才能预约");
            return map;
        }
        // 检测书本是否可借
        String copyId = request.getCopyId();
        if (borrowRepository.existsByCopyId(copyId)) {
            // 这本书正在外借,预约不得了
            map.setMessage("只能预约在库副本");
            return map;
        }
        return reserve2(request, user);
    }

    // 检测副本信息和用户规则
    private ReturnMap reserve2(ReserveRequest request, User user) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        // 检测副本信息
        Copy copy;
        copy = getCopyByCopyId(request.getCopyId());
        if (copy == null || !copy.getStatus().equals("在库")) {
            System.out.println("copy并非在库状态" + copy);
            map.setMessage("只能预约在库副本");
            return map;
        }
        // 获取用户规则
        Rule rule;
        String identity = getIdentityByUsername(user.getUsername());
        rule = ruleRepository.findRuleByIdentity(identity);
        if (rule == null) {
            map.setMessage("读者用户名不存在或身份异常,请检查");
            return map;
        }
        return reserve3(request, user, copy, rule);
    }

    // 检测最大借书数量
    private ReturnMap reserve3(ReserveRequest request, User user, Copy copy, Rule rule) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        // 检测最大借书数量
        int max = rule.getBorrowMaxNum();
        int num = borrowRepository.countByBorrower(user.getUsername());
        if (num >= max) {
            map.setMessage("预约或借书数量已达上限");
            return map;
        }
        // 检查信用分
        Integer credit = getCreditByUsername(user.getUsername());
        if (credit < 50) {
            map.setRtn(0);
            map.setMessage("用户信用不足50,支付罚单或申请恢复后才能预约");
        }
        return doReserve(request, user, copy, rule);
    }

    private ReturnMap doReserve(ReserveRequest request, User user, Copy copy, Rule rule) {
        ReturnMap map = new ReturnMap();
        DateChange now = new DateChange(new Date());
        Date timeValid = now.getDateOfSecondsLater(rule.getReserveValidTime());
        String borrower = user.getUsername();
        // 修改副本状态
        copy.setStatus("预约");
        copy.setBorrower(borrower);
        try {
            restTemplate.put("http://localhost:9091/api/book/copy", copy);
            // 本地生成新的预约
            Borrow borrow = new Borrow();
            borrow.setCopyId(copy.getCopyId());
            borrow.setTitle(copy.getTitle());
            borrow.setStatus("预约");
            borrow.setBorrower(borrower);
            borrow.setTime(now.getDate());
            borrow.setValidTime(timeValid);
            borrowRepository.save(borrow);
            // 生成系统记录
            Log log = new Log();
            log.setTitle(copy.getTitle());
            log.setCopyId(copy.getCopyId());
            log.setUsername(borrower);
            log.setCategory("预约");
            log.setPrice(0);
            log.setTime(now.getDate());
            log.setTimeValid(timeValid);
            restTemplate.put("http://localhost:9099/api/logger/log", log);
            map.setRtn(1);
            map.setMessage("预约成功");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("更新副本或系统记录接口调用失败，检查对应service");
            map.setRtn(0);
            map.setMessage("预约失败");
        }
        return map;
    }

    /**
     * getReserve 现场取预约接口
     */
    // 检测管理员和副本状态
    public ReturnMap getReserve(BorrowRequest request) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        // 检测session管理员权限
        User admin;
        admin = getUserBySession(request.getSession());
        if (admin == null || !admin.isAdmin()) {
            map.setMessage("借书需要管理员权限");
            return map;
        }
        // 检测书本是否可借
        String copyId = request.getCopyId();
        if (!borrowRepository.existsByCopyIdAndStatus(copyId, "预约")) {
            // 这本书没被预约，搞不得
            map.setMessage("需要先预约才能借书");
            return map;
        }
        return getReserve2(request, admin);
    }

    // 获取副本信息和用户规则、借书人所在地和副本所在地
    private ReturnMap getReserve2(BorrowRequest request, User admin) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        // 检测副本信息
        Copy copy;
        copy = getCopyByCopyId(request.getCopyId());
        if (copy == null || !copy.getStatus().equals("预约")) {
            System.out.println("copy并非预约状态" + copy);
            map.setMessage("需要先预约才能借书");
            return map;
        }
        // 获取用户规则
        Rule rule;
        String identity = getIdentityByUsername(request.getBorrower());
        rule = ruleRepository.findRuleByIdentity(identity);
        if (rule == null) {
            map.setMessage("读者用户名不存在或身份异常,请检查");
            return map;
        }
        return getReserve3(request, admin, copy, rule);
    }

    // 检测所在地匹配问题
    private ReturnMap getReserve3(BorrowRequest request, User admin, Copy copy, Rule rule) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        // 所在地匹配问题
        if (!request.getBranch().equals(copy.getBranch())) {
            map.setMessage(copy.getCopyId() + ":不能借阅其他分馆的副本");
            return map;
        }
        // 检查信用分
        Integer credit = getCreditByUsername(request.getBorrower());
        if (credit <= 50) {
            map.setRtn(0);
            map.setMessage("用户为0,支付罚单或申请恢复后才能取书");
        }
        return doGetReserve(request, admin, copy, rule);
    }

    private ReturnMap doGetReserve(BorrowRequest request, User admin, Copy copy, Rule rule) {
        ReturnMap map = new ReturnMap();
        DateChange now = new DateChange(new Date());
        Date timeValid = now.getDateOfSecondsLater(rule.getBorrowValidTime());
        String borrower = request.getBorrower();
        // 修改副本状态
        copy.setStatus("借出");
        copy.setBorrower(borrower);
        try {
            restTemplate.put("http://localhost:9091/api/book/copy", copy);
            // 更新本服务预约记录为借出状态
            Borrow borrow = borrowRepository.findBorrowByCopyId(copy.getCopyId());
            borrow.setStatus("借出");
            borrow.setTime(now.getDate());
            borrow.setValidTime(timeValid);
            borrowRepository.save(borrow);
            // 生成系统记录
            Log log = new Log();
            log.setTitle(copy.getTitle());
            log.setCopyId(copy.getCopyId());
            log.setUsername(borrower);
            log.setOperator(admin.getUsername());
            log.setBranch(request.getBranch());
            log.setCategory("取预约书本");
            log.setPrice(0);
            log.setTime(now.getDate());
            log.setTimeValid(timeValid);
            restTemplate.put("http://localhost:9099/api/logger/log", log);
            map.setRtn(1);
            map.setMessage("取预约成功");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("更新副本或系统记录接口调用失败，检查对应service");
            map.setRtn(0);
            map.setMessage("取预约失败");
        }
        return map;
    }

}
