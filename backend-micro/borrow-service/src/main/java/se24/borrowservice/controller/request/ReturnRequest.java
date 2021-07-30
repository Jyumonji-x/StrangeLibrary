package se24.borrowservice.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class ReturnRequest {
    @NotNull(message = "需要管理员权限才能还书")
    private String session;
    @NotNull(message = "副本编号不能为空")
    private String copyId;
    @NotNull(message = "还书分馆不能为空")
    private String branch;
    @NotNull(message = "还书状态不能为空")
    private String status;
}
