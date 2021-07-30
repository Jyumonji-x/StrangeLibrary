package se24.userservice.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class ModifyRequest {
    @NotNull(message = "只能修改当前登录用户的密码")
    private String session;
    @NotNull(message = "密码不能为空")
    private String password;
}
