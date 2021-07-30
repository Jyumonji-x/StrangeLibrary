package se24.bookservice.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
//许同樵 前端debug 应删除,但由于有很多测试存在依赖，没有删除
@Getter
@Setter
@ToString
public class BookBrowseRequest {
    private int page;
    private int size;
}

