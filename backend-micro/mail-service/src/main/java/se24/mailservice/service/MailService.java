package se24.mailservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import se24.mailservice.controller.request.SendMailRequest;
import se24.mailservice.tool.ReturnMap;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * send
     **/
    public ReturnMap send(SendMailRequest request) {
        ReturnMap map = new ReturnMap();
        map.setRtn(1);
        map.setMessage("验证码邮件发送成功");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("1534401580@qq.com");
        mailMessage.setTo(request.getTarget());
        mailMessage.setSubject(request.getTitle());
        mailMessage.setText(request.getContent());

        try {
            javaMailSender.send(mailMessage);
        } catch (Exception e) {
            System.out.println("邮件发送出现问题：");
            System.out.println("接受者为：" + request.getTarget());
            System.out.println("邮件内容为：" + request.getContent());
            System.out.println(e.getMessage());
            map.setRtn(0);
            map.setMessage("验证码邮件发送失败");
        }
        return map;
    }
}
