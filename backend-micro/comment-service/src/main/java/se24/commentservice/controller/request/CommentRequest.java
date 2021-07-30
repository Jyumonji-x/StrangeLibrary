package se24.commentservice.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class CommentRequest {
    @NotNull(message = "评论ID不能为空")
    Integer id;
    @NotNull(message = "评分不能为空")
    double rate;
    @NotNull(message = "评论标题不能为空")
    String title;
    @NotNull(message = "评论内容不能为空")
    String comment;
}
