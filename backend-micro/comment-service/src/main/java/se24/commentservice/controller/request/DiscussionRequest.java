package se24.commentservice.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class DiscussionRequest {
    @NotNull(message = "需要登录才能发布讨论")
    String session;
    @NotNull(message = "评论ID不能为空")
    int commentId;
    @NotNull(message = "讨论内容不能为空")
    String content;
}
