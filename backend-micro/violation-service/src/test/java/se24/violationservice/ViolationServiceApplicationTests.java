package se24.violationservice;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import se24.violationservice.controller.request.SendMailRequest;
import se24.violationservice.domain.Borrow;
import se24.violationservice.domain.Fine;
import se24.violationservice.domain.ReserveOverdue;
import se24.violationservice.repository.FineRepository;
import se24.violationservice.repository.OverdueRepository;
import se24.violationservice.service.BroadcastService;
import se24.violationservice.service.FineService;
import se24.violationservice.service.OverdueService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class ViolationServiceApplicationTests {
    @Mock
    FineRepository fineRepository;
    @Mock
    OverdueRepository overdueRepository;
    @Mock
    RestTemplate restTemplate;
    @InjectMocks
    BroadcastService broadcastService;
    @InjectMocks
    FineService fineService;
    @InjectMocks
    OverdueService overdueService;

    private final String mockUsername = "zmgg";
    private final String mockBook1Name = "怪书";
    private final String mockBookName = "大怪书";
    private final String copyId1 = "1234";
    private final String copyId2 = "4321";
    private final String mockReason = "有钱人不缺书";




    @Test
    void broadCast() {

        Borrow[] borrows = new Borrow[2];
        for(int i=0; i < 2; i++){
            borrows[i] = new Borrow();
            borrows[i].setBorrower(mockUsername);
        }
        borrows[0].setCopyId(copyId1);
        borrows[0].setTitle(mockBookName);

        borrows[1].setCopyId(copyId2);
        borrows[1].setTitle(mockBook1Name);



        List<ReserveOverdue> overdueList = new ArrayList<>();
        ReserveOverdue overdue1 = new ReserveOverdue();
        overdue1.setUsername(mockUsername);
        overdue1.setBookName(mockBookName);
        overdue1.setCopyId(copyId1);

        ReserveOverdue overdue2 = new ReserveOverdue();
        overdue2.setUsername(mockUsername);
        overdue2.setBookName(mockBook1Name);
        overdue1.setCopyId(copyId2);

        overdueList.add(overdue1);
        overdueList.add(overdue2);

        List<Fine> fineRecordList = new ArrayList<>();
        Fine fine1 = new Fine();
        fine1.setUsername(mockUsername);
        fine1.setBookName(mockBookName);
        fine1.setCopyId(copyId1);
        fine1.setReason(mockReason);


        Fine fine2 = new Fine();
        fine2.setUsername(mockUsername);
        fine2.setBookName(mockBook1Name);
        fine2.setCopyId(copyId2);
        fine2.setReason(mockReason);

        fineRecordList.add(fine1);
        fineRecordList.add(fine2);

        HashMap<String,String> hashMap = new HashMap<String,String>();
        ResponseEntity<HashMap> responseEntity = ResponseEntity.ok(hashMap);


        Mockito.when(restTemplate.getForObject("http://localhost:9092/api/borrow/getOverdue", Borrow[].class))
                .thenReturn(borrows);
        Mockito.when(overdueRepository.findAll()).thenReturn(overdueList);
        Mockito.when(fineRepository.findAll()).thenReturn(fineRecordList);
        Mockito.when(restTemplate.postForEntity("http://localhost:9080/api/mail/send", new SendMailRequest(), HashMap.class))
                .thenReturn(responseEntity);

        HashMap<String, Object> map = broadcastService.broadcast().getMap();
        assertEquals("消息发送成功",map.get("message"));

    }

    @Test
    void getFine(){
        List<Fine> fineRecordList = new ArrayList<>();
        Fine fine1 = new Fine();
        fine1.setUsername(mockUsername);
        fine1.setBookName(mockBookName);
        fine1.setCopyId(copyId1);
        fine1.setReason(mockReason);
        fine1.setPrice(40.0);
        Fine fine2 = new Fine();
        fine2.setUsername(mockUsername);
        fine2.setBookName(mockBook1Name);
        fine2.setCopyId(copyId2);
        fine2.setReason(mockReason);
        fine2.setPrice(470.06);

        fineRecordList.add(fine1);
        fineRecordList.add(fine2);

        Mockito.when(fineRepository.findFinesByUsername(mockUsername)).thenReturn(fineRecordList);
        HashMap<String, Object> map = fineService.getFine(mockUsername).getMap();
        assertEquals(510.06,map.get("price"));
        System.out.println(map);

    }


    @Test
    void payFine(){
        List<Fine> fineRecordList = new ArrayList<>();
        Fine fine1 = new Fine();
        fine1.setUsername(mockUsername);
        fine1.setBookName(mockBookName);
        fine1.setCopyId(copyId1);
        fine1.setReason(mockReason);
        fine1.setPrice(40.0);
        Fine fine2 = new Fine();
        fine2.setUsername(mockUsername);
        fine2.setBookName(mockBook1Name);
        fine2.setCopyId(copyId2);
        fine2.setReason(mockReason);
        fine2.setPrice(470.06);

        fineRecordList.add(fine1);
        fineRecordList.add(fine2);

        Mockito.when(fineRepository.findFinesByUsername(mockUsername)).thenReturn(fineRecordList);
        HashMap<String, Object> map = fineService.payFine(mockUsername).getMap();
        assertEquals("支付成功",map.get("message"));
        System.out.println(map);

    }

    @Test
    void getOverDue(){
        List<ReserveOverdue> overdueList = new ArrayList<>();
        ReserveOverdue overdue1 = new ReserveOverdue();
        overdue1.setUsername(mockUsername);
        overdue1.setBookName(mockBookName);
        overdue1.setCopyId(copyId1);

        ReserveOverdue overdue2 = new ReserveOverdue();
        overdue2.setUsername(mockUsername);
        overdue2.setBookName(mockBook1Name);
        overdue1.setCopyId(copyId2);

        overdueList.add(overdue1);
        Mockito.when(overdueRepository.findReserveOverduesByUsername(mockUsername)).thenReturn(overdueList);
        HashMap<String, Object> map = overdueService.getOverdue(mockUsername).getMap();
        assertEquals("查看逾期预约成功",map.get("message"));
        System.out.println(map);

    }

    @Test
    void deleteOverDue(){
        List<ReserveOverdue> overdueList = new ArrayList<>();
        ReserveOverdue overdue1 = new ReserveOverdue();
        overdue1.setUsername(mockUsername);
        overdue1.setBookName(mockBookName);
        overdue1.setCopyId(copyId1);

        ReserveOverdue overdue2 = new ReserveOverdue();
        overdue2.setUsername(mockUsername);
        overdue2.setBookName(mockBook1Name);
        overdue1.setCopyId(copyId2);

        overdueList.add(overdue1);
        Mockito.when(overdueRepository.findReserveOverduesByUsername(mockUsername)).thenReturn(overdueList);
        HashMap<String, Object> map = overdueService.deleteOverdue(mockUsername).getMap();
        assertEquals("确认逾期预约成功",map.get("message"));
        System.out.println(map);

    }
}
