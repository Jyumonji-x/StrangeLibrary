package se24.mailservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se24.mailservice.controller.request.SendMailRequest;
import se24.mailservice.service.MailService;
import se24.mailservice.tool.ReturnMap;

import javax.validation.Valid;

@RestController
public class MailController {
    private final MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/api/mail/send")
    public ResponseEntity<?> send(@RequestBody SendMailRequest request) {
        System.out.println(request);
        ReturnMap send = mailService.send(request);
        return ResponseEntity.ok(send.getMap());
    }
}
