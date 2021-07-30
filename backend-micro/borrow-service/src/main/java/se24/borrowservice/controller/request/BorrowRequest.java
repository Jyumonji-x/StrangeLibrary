package se24.borrowservice.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class BorrowRequest {
    @NotNull(message = "副本编号不能为空")
    private String copyId;
    @NotNull(message = "需要管理员权限才能借书")
    private String session;
    @NotNull(message = "借书用户名不能为空")
    private String borrower;
    @NotNull(message = "借书分馆不能为空")
    private String branch;
}
