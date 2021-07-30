package fudan.se.lab2.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class UploadChecker {
    String message = "";
    public String uploadCheck(String title, String author, String intro,
                              long ISBN, Date time_publish, MultipartFile cover){
        message = "";
        if (cover.isEmpty()) {
            message = "缺少或未接收到图书封面";
        }
        checkStringNull(title,"图书名称");
        checkStringNull(author,"图书作者");
        if (message.equals("") && ISBN == 0) {
            message = "缺少或未接收到图书ISBN";
        }
        checkStringNull(intro,"图书简介");

        if (message.equals("") && (intro.length() > 250)) {
            message = "图书简介请勿超过250字";
        }
        if (message.equals("") && time_publish == null) {
            message = "缺少或未接收到图书出版时间";
        }
        return message;
    }

    public void checkStringNull(String target, String name){
        if (message.equals("") && (target == null || target.isEmpty())){
            message += "缺少或未接收到"+name;
        }
    }
}
