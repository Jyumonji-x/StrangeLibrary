package se24.bookservice.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class UploadChecker {
    String message = "";

    public String uploadCheck(String title, String author, String intro,
                              String ISBN, Date time_publish, MultipartFile cover) {
        message = "";
        if (cover.isEmpty()) {
            message = "缺少或未接收到图书封面";
        }
        checkStringNull(title, "图书名称");
        checkStringNull(author, "图书作者");
        if (message.equals("") && ISBN.isEmpty()) {
            message = "缺少或未接收到图书ISBN";
        }
        checkStringNull(intro, "图书简介");

        if (message.equals("") && (intro.length() > 1000)) {
            message = "图书简介请勿超过1000字";
        }
        if (message.equals("") && time_publish == null) {
            message = "缺少或未接收到图书出版时间";
        }
        return message;
    }

    public void checkStringNull(String target, String name) {
        if (message.equals("") && (target == null || target.isEmpty())) {
            message += "缺少或未接收到" + name;
        }
    }
}
