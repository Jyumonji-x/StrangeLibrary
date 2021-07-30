package se24.borrowservice.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ReserveRequest {
    @NotNull(message = "需要登录才能预约")
    private String session;
    @NotNull(message = "副本编号不能为空")
    private String copyId;
}
