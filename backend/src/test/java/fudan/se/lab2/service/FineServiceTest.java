package fudan.se.lab2.service;

import fudan.se.lab2.domain.FineRecord;
import fudan.se.lab2.repository.FineRepository;
import fudan.se.lab2.repository.SubscribeOverdueRepository;
import fudan.se.lab2.util.JsonMap;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class FineServiceTest {
    @Autowired
    private FineService fineService;
    @Autowired
    private FineRepository fineRepository;

    @Test
    void getFine() {
        JsonMap map = fineService.getFine("19302010020");
        assertEquals("罚款查看成功", map.get("message"));
    }

    @Test
    void payFine() {
        JsonMap map = fineService.payFine("19302010020");
        String message = (String) map.get("message");
        boolean check = message.equals("付款成功") || message.equals("余额不足，请充值后重试");
        assertTrue(check);
    }

    @Test
    void getVoidPriceAll() {
        FineRecord fineRecord = new FineRecord();
        fineRecord.setPrice(10);

        List<FineRecord> fines = new ArrayList<>();
        assertEquals(0.00, fineService.getPriceAll(fines));
    }

    @Test
    void getPriceAll() {
        FineRecord fineRecord = new FineRecord(10.123);
        List<FineRecord> fines = new ArrayList<>();
        fines.add(fineRecord);
        assertEquals(10.13, fineService.getPriceAll(fines));
    }

    @Test
    void getPriceAllAdd() {
        List<FineRecord> fines = new ArrayList<>();
        fines.add(new FineRecord(10.0901));
        fines.add(new FineRecord(10.091));
        fines.add(new FineRecord(10.091));
        assertEquals(30.30, fineService.getPriceAll(fines));
    }
}