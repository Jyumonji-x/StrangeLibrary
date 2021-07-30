package se24.mailservice.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SendMailRequest {
    private String target;
    private String title;
    private String content;
}
