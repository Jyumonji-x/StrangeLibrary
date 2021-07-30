package se24.mailservice;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import se24.mailservice.controller.request.SendMailRequest;
import se24.mailservice.service.MailService;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class MailServiceApplicationTests {

    private JavaMailSender javaMailSender;
    private MailService mailService = new MailService(javaMailSender);


    /**
     * send
     **/

    @Test
    void sendWhenJavaMailSenderIsNULL() {
        SendMailRequest sendMailRequest = new SendMailRequest();
        HashMap<String, Object> map = mailService.send(sendMailRequest).getMap();
        assertEquals("验证码邮件发送失败", map.get("message"));
    }

}
