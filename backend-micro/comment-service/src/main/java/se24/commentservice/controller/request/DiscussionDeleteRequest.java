package se24.commentservice.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class DiscussionDeleteRequest {
    @NotNull(message = "需要登录才能删除讨论")
    String session;
    @NotNull(message = "讨论ID不能为空")
    int id;
}
