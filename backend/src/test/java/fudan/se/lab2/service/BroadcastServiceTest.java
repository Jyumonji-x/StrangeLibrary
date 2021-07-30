package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.TokenRequest;
import fudan.se.lab2.domain.*;
import fudan.se.lab2.store.MyToken;
import fudan.se.lab2.util.JsonMap;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class BroadcastServiceTest {
    @Autowired
    BroadcastService broadcastService;

    @Test
    void loadFineRecords() {
        HashMap<String, String> mailMap = new HashMap<>();
        List<FineRecord> fineRecordList = new ArrayList<>();
        FineRecord fineRecord = new FineRecord(10);
        fineRecord.setUsername("19302010020");
        fineRecord.setBookName("兄弟");
        fineRecord.setDuplicateId("1111111111-001");
        fineRecord.setReason("遗失");
        fineRecordList.add(fineRecord);
        broadcastService.loadFineRecords(mailMap, fineRecordList);
        assertEquals("您借阅的《兄弟》(1111111111-001)遗失了，根据图书馆规定，需要缴纳罚款10.0元后方可继续借阅\n",
                mailMap.get("19302010020"));
    }

    @Test
    void loadSubscribeOverMsg() {
        HashMap<String, String> mailMap = new HashMap<>();
        List<SubscribeOverdue> overdueList = new ArrayList<>();
        SubscribeOverdue overdue = new SubscribeOverdue();
        overdue.setUsername("19302010020");
        overdue.setBookName("兄弟");
        overdue.setDuplicateId("1111111111-001");
        overdueList.add(overdue);
        broadcastService.loadSubscribeOverMsg(mailMap, overdueList);
        assertEquals("您对《兄弟》(1111111111-001)的预约已过期，预约自动取消，有需要请重新预约\n",
                mailMap.get("19302010020"));
    }

    @Test
    void loadBookDuplicateMsg() {
        HashMap<String, String> mailMap = new HashMap<>();
        List<BookDuplicate> bookDuplicates = new ArrayList<>();
        BookDuplicate bookDuplicate = new BookDuplicate();
        bookDuplicate.setBorrower("19302010020");
        bookDuplicate.setTitle("兄弟");
        bookDuplicate.setDuplicateId("1111111111-001");
        bookDuplicates.add(bookDuplicate);
        broadcastService.loadBookDuplicateMsg(mailMap, bookDuplicates);
        System.out.println(mailMap);
        assertEquals("您借阅的《兄弟》(1111111111-001)已超过借阅期限，根据图书馆规定将产生费用，请尽快还书并缴纳罚款\n",
                mailMap.get("19302010020"));
    }

    @Test
    void broadcastWithPermission() {
        User user = new User();
        user.setPermission(2);
        user.setUsername("19302010020");
        Integer token = MyToken.insertToken(user);
        TokenRequest request = new TokenRequest();
        request.setToken(token);

        JsonMap map = broadcastService.broadcast(request);
        assertEquals("消息发送成功", map.get("message"));
    }

    @Test
    void broadcastWithOutPermission() {
        TokenRequest request = new TokenRequest();
        request.setToken(1);
        JsonMap map = broadcastService.broadcast(request);
        assertEquals("发送通知需要管理员权限", map.get("message"));
    }
}