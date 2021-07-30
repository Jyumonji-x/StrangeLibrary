package se24.bookservice.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class UserReserveRequest {
    @NotNull(message = "需要管理员的位置")
    private String branch;

    @NotNull(message = "需要输入用户名")
    private String username;

}
