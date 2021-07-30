package se24.userservice.controller.request;

import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Getter
@Service
@ToString
public class ApplyRequest {
    @NotNull(message = "登录才能申请恢复信用")
    String session;
    String reason;
}
