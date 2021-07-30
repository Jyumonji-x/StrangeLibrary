package fudan.se.lab2.util;

import fudan.se.lab2.domain.User;

public class DuplicateChecker {
    String message = "";
    public String  addCheck(User user,long ISBN,String location){
        message = "";
        if (user == null) {
            message = "副本入库需要管理员权限";
        }
        int permission = 0;
        if (message.equals("")) {
            permission = user.getPermission();
        }
        if (message.equals("") && permission < 1) {
            message = "书本入库需要管理员权限";
        }
        if (message.equals("") && (ISBN == 0)) {
            message = "缺少或未接收到副本ISBN";
        }
        if (message.equals("") && (location == null || location.isEmpty())) {
            message = "缺少或未接收到副本所属分馆";
        }
        return message;
    };
}
