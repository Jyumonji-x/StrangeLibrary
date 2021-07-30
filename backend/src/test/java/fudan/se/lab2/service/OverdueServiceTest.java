package fudan.se.lab2.service;

import fudan.se.lab2.domain.SubscribeOverdue;
import fudan.se.lab2.repository.SubscribeOverdueRepository;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.util.JsonMap;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class OverdueServiceTest {
    @Autowired
    private OverdueService overdueService;
    @Autowired
    private SubscribeOverdueRepository overdueRepository;

    private final String username = "19302010020";

    @Test
    void getVoidOverDue() {
        JsonMap map = overdueService.getOverdue(username);
        assertEquals("查看逾期预约成功", map.get("message"));
        assertEquals(0, ((List<SubscribeOverdue>) map.get("overdueList")).size());
    }

    @Test
    void getOverDue() {
        SubscribeOverdue subscribeOverdue = new SubscribeOverdue();
        subscribeOverdue.setUsername(username);
        overdueRepository.save(subscribeOverdue);
        JsonMap map = overdueService.getOverdue(username);
        assertEquals("查看逾期预约成功", map.get("message"));
        assertEquals(1, overdueRepository.count());
        assertEquals(1, ((List<SubscribeOverdue>) map.get("overdueList")).size());
    }

    @Test
    void deleteVoidOverDue() {
        JsonMap map = overdueService.deleteOverdue(username);
        assertEquals("确认逾期预约成功", map.get("message"));
    }

    @Test
    void deleteOverDue() {
        SubscribeOverdue subscribeOverdue = new SubscribeOverdue();
        subscribeOverdue.setUsername(username);
        overdueRepository.save(subscribeOverdue);
        JsonMap map = overdueService.deleteOverdue(username);
        assertEquals("确认逾期预约成功", map.get("message"));
        assertEquals(0, overdueRepository.count());
    }
}