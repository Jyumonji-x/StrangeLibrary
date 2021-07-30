package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.NameTokenDuplicateBranchRequest;
import fudan.se.lab2.controller.request.TokenDuplicateBranchStateRequest;
import fudan.se.lab2.controller.request.TokenDuplicateRequest;
import fudan.se.lab2.domain.*;
import fudan.se.lab2.enums.UserPermissionEnum;
import fudan.se.lab2.store.MyToken;
import fudan.se.lab2.repository.*;
import fudan.se.lab2.util.BorrowChecker;
import fudan.se.lab2.util.DateChange;
import fudan.se.lab2.util.JsonMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class BorrowService {
    private final OverdueService overdueService;
    private final BookRepository bookRepository;
    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;
    private final DuplicateRepository duplicateRepository;
    private final RuleRepository ruleRepository;
    private final FineRepository fineRepository;


    @Autowired
    public BorrowService(OverdueService overdueService,BorrowRepository borrowRepository,UserRepository userRepository
            ,DuplicateRepository duplicateRepository,RuleRepository ruleRepository
            ,BookRepository bookRepository,FineRepository fineRepository) {
        this.overdueService = overdueService;
        this.borrowRepository = borrowRepository;
        this.userRepository = userRepository;
        this.duplicateRepository = duplicateRepository;
        this.ruleRepository = ruleRepository;
        this.bookRepository = bookRepository;
        this.fineRepository = fineRepository;
    }

    public JsonMap getHistoryByUsername(String username) {
        JsonMap map = new JsonMap();
        int rtn = 1;
        String message = "查询成功";
        try {
            List<BookBorrow> history = borrowRepository.findBookBorrowsByUsernameOrderByTimeDesc(username);
            map.put("history",history);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rtn = 0;
            message = "查询失败";
        }
        map.put("rtn",rtn);
        map.put("message",message);
        return map;
    }

    public JsonMap getHistoryByDuplicateId(String duplicateId) {
        JsonMap map = new JsonMap();
        int rtn = 1;
        String message = "查询成功";
        try {
            List<BookBorrow> history = borrowRepository.findBookBorrowsByDuplicateIdOrderByTimeDesc(duplicateId);
            map.put("history",history);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rtn = 0;
            message = "查询失败";
        }
        map.put("rtn",rtn);
        map.put("message",message);
        return map;
    }

    public JsonMap getHistory() {
        JsonMap map = new JsonMap();
        int rtn = 1;
        String message = "查询成功";
        try {
            List<BookBorrow> history = borrowRepository.findAllByOrderByTimeDesc();
            map.put("history",history);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rtn = 0;
            message = "查询失败";
        }
        map.put("rtn",rtn);
        map.put("message",message);
        return map;
    }

    public JsonMap borrowed(String userName,int token) {
        System.out.println("BorrowService.borrowed() called");
        System.out.println("userName = " + userName);
        System.out.println("token = " + token);

        //----------洗库，检查预约过期--------------
        overdueService.subscribeOverdueCheck();

        JsonMap resultMap = new JsonMap();
        int rtn = 1;
        String message = "查询借阅记录成功";
        User user = MyToken.getUserByTokenId(token);
        if (user == null) {
            rtn = 0;
            message = "需要登录";
        }
        if (rtn == 1 && (!user.getUsername().equals(userName) && user.getPermission() == 0)) {
            rtn = 0;
            message = "需要用户本人或管理员权限";
        }
        if (rtn == 1 && userRepository.findByUsername(userName) == null) {
            rtn = 0;
            message = "用户名不存在，请重试";
        }
        if (rtn == 1) {
            message = "查询借阅记录成功";
            List<BookDuplicate> duplicates = duplicateRepository.findBookDuplicatesByBorrower(userName);
            resultMap.put("bookBorrow",duplicates);
        }
        resultMap.put("rtn",rtn);
        resultMap.put("message",message);
        return resultMap;
    }

    private String subscribeBookChecker(int token,String duplicate_id) {
        String message = "";
        // 用户检测
        User user = MyToken.getUserByTokenId(token);
        BookDuplicate duplicate = duplicateRepository.findBookDuplicateByDuplicateId(duplicate_id);
        if (user == null) {
            message = "需要登录才能预约";
        }
        else if (user.getPermission() != UserPermissionEnum.normalUser.getPermission()) {
            message = "管理员无法预约";
        }
        else if (duplicate == null) {
            message = "副本不存在";
        }
        else if (!duplicate.getStatus().equals("在库")) {
            message = "只能预约在库书籍:" + duplicate_id;
        }
        else if (fineRepository.findFineRecordsByUsername(user.getUsername()).size() != 0) {
            // 检测用户有无罚款未付
            message = "请先付清罚款再预约";
        }
        return message;
    }

    // 用户选择在库图书进行预约，更改副本status，创建bookBorrow
    public JsonMap subscribeBook(TokenDuplicateRequest request) {
        int token = request.getToken();
        String duplicate_id = request.getDuplicate_id();
        JsonMap resultMap = new JsonMap();
        int rtn = 1;
        String message = "";
        message = subscribeBookChecker(token,duplicate_id);
        if (message.equals("")) {
            User user = MyToken.getUserByTokenId(token);
            BookDuplicate duplicate = duplicateRepository.findBookDuplicateByDuplicateId(duplicate_id);

            String identity = user.getIdentity();
            BorrowRule borrowRule = ruleRepository.findBorrowRuleByIdentity(identity);

            // 检测预约是否超出上限
            String username = user.getUsername();
            if (duplicateRepository.countBookDuplicatesByBorrowerAndStatus(username,"预约") >= borrowRule.getMaxBorrowNumber()) {
                rtn = 0;
                message = "预约数量已达到上限";
            }
            if (rtn == 1) {
                // 进行预约
                Date now = new Date();
                duplicate.setTimeStart(now);
                DateChange dateChange = new DateChange(now);
                System.out.println(borrowRule);
                Date validDate = dateChange.getDateOfSecondsLater(borrowRule.getSubscribeValidPeriod());

                duplicate.setStatus("预约");
                duplicate.setBorrower(user.getUsername());
                duplicate.setTimeValid(validDate);
                duplicateRepository.save(duplicate);
                // 新增预约记录
                BookBorrow borrow = new BookBorrow();
                borrow.setBookName(duplicate.getTitle());
                borrow.setUsername(user.getUsername());
                // 预约操作没有管理员 不设定
                borrow.setDuplicateId(duplicate_id);
                borrow.setBranch(duplicate.getBranch());
                borrow.setCategory("预约");
                borrow.setTime(now);
                borrow.setTimeValid(validDate);
                borrowRepository.save(borrow);
                message = "预约成功";
            }
        }
        else {
            rtn = 0;
        }
        resultMap.put("rtn",rtn);
        resultMap.put("message",message);
        return resultMap;
    }

    private String translateReturnStatusToHistory(String status) {
        if (status.equals("完好")) {
            return "归还";
        }
        else {
            return status;
        }
    }

    private String translateReturnStatusToCopy(String status) {
        if (status.equals("完好")) {
            return "在库";
        }
        else {
            return status;
        }
    }

    // 还书：更改副本status，更改为归还的bookBorrow中的各种属性
    public JsonMap returnBook(TokenDuplicateBranchStateRequest request) {
        int token = request.getToken();
        String duplicate_id = request.getDuplicate_id();
        String branch = request.getBranch();
        JsonMap resultMap = new JsonMap();
        int rtn = 1;
        String message = "";
        // 检测操作人的管理员权限
        User admin = MyToken.getUserByTokenId(token);
        if (!MyToken.isAdmin(token)) {
            rtn = 0;
            message = "还书需要管理员权限";
        }

        BookDuplicate duplicate = duplicateRepository.findBookDuplicateByDuplicateId(duplicate_id);
        if (rtn == 1) {
            message = BorrowChecker.returnChecker(duplicate,duplicate_id,branch);
            if (!message.equals("")) {
                rtn = 0;
            }
        }
        // 进行书籍归还
        if (rtn == 1) {
            String username = duplicate.getBorrower();
            message = "书籍归还成功";
            Date now = new Date();
            // 添加归还记录
            BookBorrow borrow = new BookBorrow();
            borrow.setBookName(duplicate.getTitle());
            borrow.setUsername(username);
            borrow.setOperatorName(admin.getUsername());
            borrow.setDuplicateId(duplicate_id);
            borrow.setBranch(branch);
            borrow.setCategory(translateReturnStatusToHistory(request.getStatus()));
            borrow.setTime(now);
            borrowRepository.save(borrow);

            // 进行归还
            Book book = bookRepository.findBookByISBN(duplicate.getISBN());
            boolean needFine = false;
            double finePrice = 0;
            String fineCategory = "";
            switch (request.getStatus()) {
                case "完好":
                    if (now.after(duplicate.getTimeValid())) {
                        needFine = true;
                        finePrice = (book.getPrice() / 4);
                        fineCategory = "借阅逾期";
                        message = "归还成功(超出借阅期限，缴清罚款后方可再次借书)";
                    }
                    break;
                case "损坏":
                    needFine = true;
                    finePrice = (book.getPrice() / 2);
                    fineCategory = "损坏";
                    message = "图书损坏，缴清罚款后方可再次借书";
                    break;
                case "遗失":
                    needFine = true;
                    finePrice = (book.getPrice());
                    fineCategory = "遗失";
                    message = "图书遗失，缴清罚款后方可再次借书";
                    break;
            }
            // 罚款小数点后2位向上取整
            BigDecimal priceBD = BigDecimal.valueOf(finePrice);
            finePrice = priceBD.setScale(2,RoundingMode.CEILING).doubleValue();
            if (needFine) {
                // 产生罚单，产生记录
                FineRecord fineRecord = new FineRecord();
                fineRecord.setReason(fineCategory);
                fineRecord.setBookName(book.getTitle());
                fineRecord.setDuplicateId(duplicate_id);
                fineRecord.setUsername(duplicate.getBorrower());
                fineRecord.setPrice(finePrice);
                fineRepository.save(fineRecord);
                BookBorrow fine_history = new BookBorrow();
                fine_history.setBookName(book.getTitle());
                fine_history.setBranch(branch);
                fine_history.setCategory(fineCategory);
                fine_history.setDuplicateId(duplicate_id);
                fine_history.setOperatorName(admin.getUsername());
                fine_history.setTime(now);
                fine_history.setUsername(username);
                fine_history.setPrice(finePrice);
                borrowRepository.save(fine_history);
            }
            duplicate.setStatus(translateReturnStatusToCopy(request.getStatus()));
            duplicate.setBranch(branch);
            // 清空跟预约和借书相关的属性
            duplicate.setTimeStart(null);
            duplicate.setTimeValid(null);
            duplicate.setBorrower(null);
            duplicateRepository.save(duplicate);
        }
        resultMap.put("rtn",rtn);
        resultMap.put("message",message);
        return resultMap;
    }

    // 取出预约的书籍：更改副本状态，更改bookBorrow的各种属性
    public JsonMap getSubscribeBook(NameTokenDuplicateBranchRequest request) {
        int token = request.getToken();
        String duplicate_id = request.getDuplicate_id();
        String branch = request.getBranch();

        JsonMap resultMap = new JsonMap();
        int rtn = 1;
        String message = "";

        // 检查token权限是否为管理员
        User admin = MyToken.getUserByTokenId(token);
        if (!MyToken.isAdmin(token)) {
            rtn = 0;
            message = "取书需要管理员权限";
        }
        BookDuplicate duplicate = duplicateRepository.findBookDuplicateByDuplicateId(duplicate_id);
        if (rtn == 1 && fineRepository.findFineRecordsByUsername(duplicate.getBorrower()).size() != 0) {
            // 检测用户有无罚款未付
            rtn = 0;
            message = "请先付清罚款再取书";
        }
        // 检测借书是否超出上限
        String username = duplicate.getBorrower();
        User user = userRepository.findByUsername(username);
        if (rtn == 1 && duplicateRepository.countBookDuplicatesByBorrowerAndStatus(username,"借出") >= ruleRepository.findBorrowRuleByIdentity(user.getIdentity()).getMaxBorrowNumber()) {
            rtn = 0;
            message = "借书数量已达到上限";
        }
        // 检查书本是否被预约
        if (rtn == 1) {
            message = BorrowChecker.getSubscribeChecker(duplicate,duplicate_id,branch);
            if (!message.equals("")) {
                rtn = 0;
            }
        }
        if (rtn == 1) {
            String identity = user.getIdentity();
            BorrowRule borrowRule = ruleRepository.findBorrowRuleByIdentity(identity);
            Date now = new Date();
            DateChange dateChange = new DateChange(now);
            Date validTime = dateChange.getDateOfSecondsLater(borrowRule.getBorrowValidPeriod());

            // 取出副本
            duplicate.setTimeStart(now);
            duplicate.setTimeValid(validTime);
            duplicate.setStatus("借出");
            duplicateRepository.save(duplicate);

            // 产生取书记录
            BookBorrow borrow = new BookBorrow();
            borrow.setBookName(duplicate.getTitle());
            borrow.setUsername(duplicate.getBorrower());
            borrow.setOperatorName(admin.getUsername());
            borrow.setDuplicateId(duplicate_id);
            borrow.setBranch(branch);
            borrow.setCategory("取书");
            borrow.setTime(now);
            borrow.setTimeValid(validTime);
            borrowRepository.save(borrow);
            message = "现场取书成功";
        }
        resultMap.put("rtn",rtn);
        resultMap.put("message",message);
        return resultMap;
    }

    public String borrowBookChecker(String user_name) {
        User user = userRepository.findByUsername(user_name);
        String message = "";
        if (user == null) {
            message = "用户未注册，无法借书";
        }
        else if (user.getPermission() != UserPermissionEnum.normalUser.getPermission()) {
            message = "管理员无法借书";
        }
        else if (fineRepository.findFineRecordsByUsername(user_name).size() != 0) {
            message = "请先付清罚款再预约";
        }
        return message;
    }


    // 现场借书：更改副本状态，新建一条bookBorrow记录
    public JsonMap borrowBook(NameTokenDuplicateBranchRequest request) {
        int token = request.getToken();
        String duplicate_id = request.getDuplicate_id();
        String borrow_user_name = request.getBorrow_name();
        String branch = request.getBranch();
        JsonMap resultMap = new JsonMap();
        int rtn = 1;
        String message = "";
        // 检测管理员权限
        if (!MyToken.isAdmin(token)) {
            rtn = 0;
            message = "现场借书需要管理员权限";
        }
        if (rtn == 1) {
            message = borrowBookChecker(borrow_user_name);
            if (!message.equals("")) {
                rtn = 0;
            }
        }
        // 检测借书是否超出上限
        User user = userRepository.findByUsername(borrow_user_name);
        if (rtn == 1 && duplicateRepository.countBookDuplicatesByBorrowerAndStatus(borrow_user_name,"借出") >= ruleRepository.findBorrowRuleByIdentity(user.getIdentity()).getMaxBorrowNumber()) {
            rtn = 0;
            message = "借书数量已达到上限";
        }
        User admin = MyToken.getUserByTokenId(token);
        // 检测书本是否可借
        BookDuplicate duplicate = duplicateRepository.findBookDuplicateByDuplicateId(duplicate_id);
        if (rtn == 1) {
            message = BorrowChecker.borrowChecker(duplicate,duplicate_id,branch);
            if (!message.equals("")) {
                rtn = 0;
            }
        }
        if (rtn == 1) {
            String identity = user.getIdentity();
            BorrowRule borrowRule = ruleRepository.findBorrowRuleByIdentity(identity);
            Date now = new Date();
            DateChange dateChange = new DateChange(now);
            Date validTime = dateChange.getDateOfSecondsLater(borrowRule.getBorrowValidPeriod());

            // 更改副本状态
            duplicate.setTimeStart(now);
            duplicate.setTimeValid(validTime);
            duplicate.setStatus("借出");
            duplicate.setBorrower(borrow_user_name);
            duplicateRepository.save(duplicate);

            // 产生取书记录
            BookBorrow borrow = new BookBorrow();
            borrow.setBookName(duplicate.getTitle());
            borrow.setUsername(duplicate.getBorrower());
            borrow.setOperatorName(admin.getUsername());
            borrow.setDuplicateId(duplicate_id);
            borrow.setBranch(branch);
            borrow.setCategory("借书");
            borrow.setTime(now);
            borrow.setTimeValid(validTime);
            borrowRepository.save(borrow);
            message = "借书成功";
        }
        resultMap.put("rtn",rtn);
        resultMap.put("message",message);
        return resultMap;
    }


}
