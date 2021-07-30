package se24.borrowservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import se24.borrowservice.domain.Borrow;
import se24.borrowservice.domain.Log;
import se24.borrowservice.domain.ReserveOverdue;
import se24.borrowservice.repository.BorrowRepository;

import java.util.Date;
import java.util.List;

@Configuration
@EnableScheduling
public class ScheduleTask {
    private final RestTemplate restTemplate;
    private final BorrowRepository borrowRepository;

    @Autowired
    public ScheduleTask(RestTemplate restTemplate, BorrowRepository borrowRepository) {
        this.restTemplate = restTemplate;
        this.borrowRepository = borrowRepository;
    }

    @Scheduled(fixedDelay = 10 * 1000)// 执行结束后10秒再次执行
    private void checkReserveValidation() {
        // 查找逾期预约
        Date now = new Date();
        List<Borrow> borrows = borrowRepository.findBorrowsByValidTimeBeforeAndStatus(now, "预约");
        for (Borrow borrow : borrows) {
            // 计入提醒栈
            ReserveOverdue reserveOverdue = new ReserveOverdue();
            reserveOverdue.setBookName(borrow.getTitle());
            reserveOverdue.setCopyId(borrow.getCopyId());
            reserveOverdue.setUsername(borrow.getBorrower());
            restTemplate.put("http://localhost:9094/api/overdue/add", reserveOverdue);
            // 留下系统记录
            Log log = new Log();
            log.setTitle(borrow.getTitle());
            log.setCopyId(borrow.getCopyId());
            log.setUsername(borrow.getBorrower());
            log.setCategory("预约逾期自动归还");
            log.setNote("信用积分-10");
            log.setTime(now);
            restTemplate.put("http://localhost:9099/api/logger/log", log);
            // 扣信用分
            restTemplate.put("http://localhost:9090/api/credit/" + borrow.getBorrower() + "/" + "-10", null);
            // 归还每本书
            restTemplate.put("http://localhost:9091/api/book/copy/" + borrow.getCopyId() + "/" + "在库", null);
        }
        // 本服务中取消预约
        borrowRepository.deleteAll(borrows);
        System.out.println("预约逾期自动归还执行完毕，共归还" + borrows.size() + "本");
        System.out.println(borrows);
    }
}
