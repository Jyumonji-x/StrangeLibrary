package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.TokenRequest;
import fudan.se.lab2.domain.BookDuplicate;
import fudan.se.lab2.domain.FineRecord;
import fudan.se.lab2.domain.SubscribeOverdue;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.store.MyToken;
import fudan.se.lab2.repository.DuplicateRepository;
import fudan.se.lab2.repository.FineRepository;
import fudan.se.lab2.repository.SubscribeOverdueRepository;
import fudan.se.lab2.util.JsonMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BroadcastService {
    private final SubscribeOverdueRepository subscribeOverdueRepository;
    private final DuplicateRepository duplicateRepository;
    private final FineRepository fineRepository;
    private final JavaMailSender javaMailSender;

    @Autowired
    public BroadcastService(SubscribeOverdueRepository subscribeOverdueRepository,DuplicateRepository duplicateRepository,FineRepository fineRepository
            ,JavaMailSender javaMailSender) {
        this.subscribeOverdueRepository = subscribeOverdueRepository;
        this.duplicateRepository = duplicateRepository;
        this.fineRepository = fineRepository;
        this.javaMailSender = javaMailSender;
    }

    public void loadFineRecords(HashMap<String,String> mailMap,List<FineRecord> fineRecordList) {
        for (FineRecord fineRecord : fineRecordList) {
            String username = fineRecord.getUsername();
            String bookName = fineRecord.getBookName();
            String reason = fineRecord.getReason();
            String duplicateId = fineRecord.getDuplicateId();
            double price = fineRecord.getPrice();
            if (mailMap.containsKey(username)) {
                String updateMsg = mailMap.get(username) + getFineMsg(bookName,duplicateId,reason,price);
                mailMap.put(username,updateMsg);
            }
            else {
                mailMap.put(username,getFineMsg(bookName,duplicateId,reason,price));
            }
        }
    }

    public void loadSubscribeOverMsg(HashMap<String,String> mailMap,List<SubscribeOverdue> overdueList) {
        for (SubscribeOverdue overdue : overdueList) {
            String username = overdue.getUsername();
            String bookName = overdue.getBookName();
            String duplicateId = overdue.getDuplicateId();
            if (mailMap.containsKey(username)) {
                String updatedMsg = mailMap.get(username) + getSubscribeOverdueMsg(bookName,duplicateId);
                mailMap.put(username,updatedMsg);
            }
            else {
                mailMap.put(username,getSubscribeOverdueMsg(bookName,duplicateId));
            }
        }
    }

    public void loadBookDuplicateMsg(HashMap<String,String> mailMap,List<BookDuplicate> duplicateList) {
        for (BookDuplicate duplicate : duplicateList) {
            String username = duplicate.getBorrower();
            String bookName = duplicate.getTitle();
            String duplicateId = duplicate.getDuplicateId();
            if (mailMap.containsKey(username)) {
                String updateMsg = mailMap.get(username) + getBorrowOverdueMsg(bookName,duplicateId);
                mailMap.put(username,updateMsg);
            }
            else {
                mailMap.put(username,getBorrowOverdueMsg(bookName,duplicateId));
            }
        }
    }

    public JsonMap broadcast(TokenRequest request) {
        JsonMap map = new JsonMap();
        int rtn = 1;
        String message = "消息发送成功";

        int token = request.getToken();

        // 检测管理员权限
        if (!MyToken.isAdmin(token)) {
            rtn = 0;
            message = "发送通知需要管理员权限";
        }
        HashMap<String,String> mailMap = new HashMap<>();
        // 遍历逾期预约数据库、已外借书本、已生成罚金，按用户统计并发送邮件
        List<SubscribeOverdue> overdueList = subscribeOverdueRepository.findAll();
        List<FineRecord> fineRecordList = fineRepository.findAll();
        // List<BookDuplicate> duplicateList = duplicateRepository.findBookDuplicatesByTimeValidBefore(new Date());
        List<BookDuplicate> duplicateList = duplicateRepository.findBookDuplicatesByTimeValidBeforeAndStatus(new Date(),"借出");
        loadSubscribeOverMsg(mailMap,overdueList);
        loadFineRecords(mailMap,fineRecordList);
        loadBookDuplicateMsg(mailMap,duplicateList);
        for (Map.Entry<String,String> entry : mailMap.entrySet()) {
            String username = entry.getKey();
            String text = entry.getValue();
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom("1534401580@qq.com");
            mail.setTo(username + "@fudan.edu.cn");
            mail.setSubject("StrangeLibrary:您有待处理的消息，请前往图书馆查看");
            mail.setText(text);
            try {
                System.out.println("尝试发送邮件到：" + username + "@fudan.edu.cn");
                javaMailSender.send(mail);
            } catch (Exception e) {
                System.out.println("邮件发送出现问题：");
                System.out.println("接受者为：" + username);
                System.out.println("邮件内容为：" + text);
                System.out.println(e.getMessage());
            }

            // 删除已发送的超期消息队列
            subscribeOverdueRepository.deleteAll(overdueList);
        }
        map.put("rtn",rtn);
        map.put("message",message);
        return map;
    }

    private String getSubscribeOverdueMsg(String bookName,String duplicateId) {
        return "您对《" + bookName + "》(" + duplicateId + ")" + "的预约已过期，预约自动取消，有需要请重新预约\n";
    }

    private String getBorrowOverdueMsg(String bookName,String duplicateId) {
        return "您借阅的《" + bookName + "》(" + duplicateId + ")" + "已超过借阅期限，根据图书馆规定将产生费用，请尽快还书并缴纳罚款\n";
    }

    private String getFineMsg(String bookName,String duplicateId,String reason,double price) {
        return "您借阅的《" + bookName + "》(" + duplicateId + ")" + reason + "了，根据图书馆规定，需要缴纳罚款" + price + "元后方可继续借阅\n";
    }
}
