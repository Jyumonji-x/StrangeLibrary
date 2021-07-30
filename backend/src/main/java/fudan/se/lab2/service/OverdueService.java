package fudan.se.lab2.service;

import fudan.se.lab2.domain.Book;
import fudan.se.lab2.domain.BookBorrow;
import fudan.se.lab2.domain.BookDuplicate;
import fudan.se.lab2.domain.SubscribeOverdue;
import fudan.se.lab2.repository.BorrowRepository;
import fudan.se.lab2.repository.DuplicateRepository;
import fudan.se.lab2.repository.SubscribeOverdueRepository;
import fudan.se.lab2.util.JsonMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OverdueService {
    private final SubscribeOverdueRepository overdueRepository;
    private final DuplicateRepository duplicateRepository;
    private final BorrowRepository borrowRepository;

    @Autowired
    public OverdueService(SubscribeOverdueRepository overdueRepository, DuplicateRepository duplicateRepository, BorrowRepository borrowRepository) {
        this.overdueRepository = overdueRepository;
        this.duplicateRepository = duplicateRepository;
        this.borrowRepository = borrowRepository;
    }


    public JsonMap getOverdue(String username) {
        JsonMap map = new JsonMap();
        int rtn = 1;
        String message = "查看逾期预约成功";
        List<SubscribeOverdue> overdueList = overdueRepository.findSubscribeOverduesByUsername(username);
        map.put("rtn", rtn);
        map.put("message", message);
        map.put("overdueList", overdueList);
        return map;
    }

    public JsonMap deleteOverdue(String username) {
        JsonMap map = new JsonMap();
        int rtn = 1;
        String message = "确认逾期预约成功";
        try {
            overdueRepository.deleteSubscribeOverduesByUsername(username);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rtn = 0;
            message = "确认预期失败，请重试";
        }
        map.put("rtn", rtn);
        map.put("message", message);
        return map;
    }


    public void subscribeOverdueCheck() {
        System.out.println("预约过期洗库函数");
        //----------洗库，检查预约过期--------------
//List<BookDuplicate> subscribeBooks = duplicateRepository.findBookDuplicatesByStatus("预约");
        Date now = new Date();
        List<BookDuplicate> subscribeBooks = duplicateRepository.findBookDuplicatesByTimeValidBeforeAndStatus(now, "预约");
        System.out.println("目前超期的预约：" + subscribeBooks);
        for (BookDuplicate duplicate : subscribeBooks) {
            //返库
            duplicate.setStatus("在库");
            duplicateRepository.saveAndFlush(duplicate);
        }
        for (BookDuplicate duplicate : subscribeBooks) {
            SubscribeOverdue subscribeOverdue = new SubscribeOverdue();
            subscribeOverdue.setBookName(duplicate.getTitle());
            subscribeOverdue.setDuplicateId(duplicate.getDuplicateId());
            subscribeOverdue.setUsername(duplicate.getBorrower());

            overdueRepository.save(subscribeOverdue);

            // 新增预约过期记录
            BookBorrow log = new BookBorrow();
            log.setBookName(duplicate.getTitle());
            log.setUsername(duplicate.getBorrower());
            // 预约操作没有管理员 不设定
            log.setDuplicateId(duplicate.getDuplicateId());
            log.setBranch(duplicate.getBranch());
            log.setCategory("预约逾期");
            log.setTime(now);
            log.setTimeValid(null);
            borrowRepository.save(log);
        }
        for (BookDuplicate duplicate : subscribeBooks) {
            //返库
            // 清空跟预约和借书相关的属性
            duplicate.setTimeStart(null);
            duplicate.setTimeValid(null);
            duplicate.setBorrower(null);
            duplicateRepository.save(duplicate);
        }
    }
}
