package se24.violationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se24.violationservice.controller.request.SendMailRequest;
import se24.violationservice.domain.Borrow;
import se24.violationservice.domain.Fine;
import se24.violationservice.domain.ReserveOverdue;
import se24.violationservice.repository.FineRepository;
import se24.violationservice.repository.OverdueRepository;
import se24.violationservice.tool.ReturnMap;

import java.util.*;

@Service
public class BroadcastService {
    private final FineRepository fineRepository;
    private final OverdueRepository overdueRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public BroadcastService(FineRepository fineRepository, OverdueRepository overdueRepository, RestTemplate restTemplate) {
        this.fineRepository = fineRepository;
        this.overdueRepository = overdueRepository;
        this.restTemplate = restTemplate;
    }

    public void loadFines(HashMap<String, String> mailMap, List<Fine> FineList) {
        for (Fine fine : FineList) {
            String username = fine.getUsername();
            String bookName = fine.getBookName();
            String reason = fine.getReason();
            String duplicateId = fine.getCopyId();
            double price = fine.getPrice();
            if (mailMap.containsKey(username)) {
                String updateMsg = mailMap.get(username) + getFineMsg(bookName, duplicateId, reason, price);
                mailMap.put(username, updateMsg);
            } else {
                mailMap.put(username, getFineMsg(bookName, duplicateId, reason, price));
            }
        }
    }

    public void loadSubscribeOverMsg(HashMap<String, String> mailMap, List<ReserveOverdue> overdueList) {
        for (ReserveOverdue overdue : overdueList) {
            String username = overdue.getUsername();
            String bookName = overdue.getBookName();
            String duplicateId = overdue.getCopyId();
            if (mailMap.containsKey(username)) {
                String updatedMsg = mailMap.get(username) + getReserveOverdueMsg(bookName, duplicateId);
                mailMap.put(username, updatedMsg);
            } else {
                mailMap.put(username, getReserveOverdueMsg(bookName, duplicateId));
            }
        }
    }

    public void loadBorrowMsg(HashMap<String, String> mailMap, List<Borrow> borrowList) {
        for (Borrow borrow : borrowList) {
            String username = borrow.getBorrower();
            String bookName = borrow.getTitle();
            String duplicateId = borrow.getCopyId();
            if (mailMap.containsKey(username)) {
                String updateMsg = mailMap.get(username) + getBorrowOverdueMsg(bookName, duplicateId);
                mailMap.put(username, updateMsg);
            } else {
                mailMap.put(username, getBorrowOverdueMsg(bookName, duplicateId));
            }
        }
    }

    public ReturnMap broadcast() {
        ReturnMap map = new ReturnMap();
        int rtn = 1;
        String message = "消息发送成功";
        HashMap<String, String> mailMap = new HashMap<>();

        // 遍历逾期预约数据库、已外借书本、已生成罚金，按用户统计并发送邮件
        Borrow[] borrows = restTemplate.getForObject("http://localhost:9092/api/borrow/getOverdue", Borrow[].class);
        if (borrows==null){borrows=new Borrow[0];}
        List<Borrow> borrowList= Arrays.asList(borrows);
        List<ReserveOverdue> overdueList = overdueRepository.findAll();
        List<Fine> fineRecordList = fineRepository.findAll();
        // 这里需要向Borrow获取所有逾期借阅
        loadSubscribeOverMsg(mailMap, overdueList);
        loadFines(mailMap, fineRecordList);
        loadBorrowMsg(mailMap, borrowList);
        for (Map.Entry<String, String> entry : mailMap.entrySet()) {
            String username = entry.getKey();
            String text = entry.getValue();

            SendMailRequest request = new SendMailRequest();
            request.setTarget(username + "@fudan.edu.cn");
            request.setTitle("StrangeLibrary:您有待处理的消息，请前往图书馆查看");
            request.setContent(text);
            try {
                System.out.println("尝试发送邮件到：" + username + "@fudan.edu.cn");
                ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity("http://localhost:9080/api/mail/send", request, HashMap.class);
                HashMap<String, Object> repMap = responseEntity.getBody();
                map.setMap(repMap);
            } catch (Exception e) {
                System.out.println("邮件发送出现问题：");
                System.out.println("接受者为：" + username);
                System.out.println("邮件内容为：" + text);
                System.out.println(e.getMessage());
            }
            // 删除已发送的超期消息队列
            overdueRepository.deleteAll(overdueList);
        }
        map.put("rtn", rtn);
        map.put("message", message);
        return map;
    }

    private String getReserveOverdueMsg(String bookName, String copyId) {
        return "您对《" + bookName + "》(" + copyId + ")" + "的预约已过期，预约自动取消，有需要请重新预约\n";
    }

    private String getBorrowOverdueMsg(String bookName, String copyId) {
        return "您借阅的《" + bookName + "》(" + copyId + ")" + "已超过借阅期限，根据图书馆规定将产生费用，请尽快还书并缴纳罚款\n";
    }

    private String getFineMsg(String bookName, String copyId, String reason, double price) {
        return "您借阅的《" + bookName + "》(" + copyId + ")" + reason + "了，根据图书馆规定，需要缴纳罚款" + price + "元后方可继续借阅\n";
    }
}
