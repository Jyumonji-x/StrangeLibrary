package se24.commentservice.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class DeleteOrHideCommentRequest {
    @NotNull(message = "需要已登录用户才能还书")
    private String session;
    @NotNull(message = "评论编号不能为空")
    private Integer id;
}
