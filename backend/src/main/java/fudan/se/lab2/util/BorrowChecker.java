package fudan.se.lab2.util;

import fudan.se.lab2.domain.BookDuplicate;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.enums.UserPermissionEnum;
import fudan.se.lab2.store.MyToken;

import java.util.Objects;

public class BorrowChecker {
    public static String returnChecker(BookDuplicate duplicate, String duplicate_id, String branch) {
        String message = "";
        // 检测参数
        if (duplicate_id.isEmpty()) {
            message = "副本编号不能为空";
        }
        if (message.equals("") && branch.isEmpty()) {
            message = "还书分馆不能为空";
        }
        // 检测副本表中副本状态
        if (message.equals("") && duplicate == null) {
            message = "副本不存在";
        }
        if (message.equals("") && !Objects.equals(duplicate.getStatus(), "借出")) {
            message = "不能归还未被借出的副本:" + duplicate_id;
        }
        return message;
    }

    public static String getSubscribeChecker(BookDuplicate duplicate, String duplicate_id, String branch) {
        String message = "";
        if (duplicate == null) {
            message = "副本不存在";
        }
        if (message.equals("") && !Objects.equals(duplicate.getStatus(), "预约")) {
            message = "不能取出未被预约的书籍,请检查书本状态:" + duplicate_id;
        }
        if (message.equals("") && !duplicate.getBranch().equals(branch)) {
            message = "不能取出其他分馆书本:" + duplicate_id;
        }
        return message;
    }

    public static String borrowChecker(BookDuplicate duplicate, String duplicate_id, String branch) {
        String message = "";
        if (duplicate == null) {
            message = "副本不存在";
        }

        if (message.equals("") && !Objects.equals(duplicate.getStatus(), "在库")) {
            message = "不能借阅非在库书本:" + duplicate_id;
        }
        if (message.equals("") && !Objects.equals(duplicate.getBranch(), branch)) {
            message = "不能借阅其他分馆的书本:" + duplicate_id;
        }
        return message;
    }
}
