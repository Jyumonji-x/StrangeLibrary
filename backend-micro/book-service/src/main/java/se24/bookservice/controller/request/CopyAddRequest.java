package se24.bookservice.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class CopyAddRequest {
    @NotNull(message = "需要管理员权限创建副本")
    private String session;

    @JsonProperty(value = "ISBN")
    @NotNull(message = "请指定新增副本的ISBN")
    private String ISBN;

    @NotNull(message = "请指定新增副本的分馆")
    private String location;

    @NotNull(message = "请指定新增副本的数量")
    private int number;
}
