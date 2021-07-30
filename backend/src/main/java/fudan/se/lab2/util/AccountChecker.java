package fudan.se.lab2.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class AccountChecker {
    private final RegexUtil regexUtil;
    private final UserRepository userRepository;

    public AccountChecker(UserRepository userRepository) {
        this.regexUtil = new RegexUtil();
        this.userRepository = userRepository;
    }

    public String checkUserName(String userName) throws JsonProcessingException {
        System.out.println("AuthService.checkUserName called");
        Map<String, Object> resultMap = new HashMap<>();
        User user = userRepository.findByUsername(userName);

        if (user != null) {
            resultMap.put("rtn", 1);
            resultMap.put("message", "查询成功");
            resultMap.put("user_name_exist", true);
        } else {
            resultMap.put("rtn", 1);
            resultMap.put("message", "查询成功");
            resultMap.put("user_name_exist", false);
        }
        return new ObjectMapper().writeValueAsString(resultMap);
    }

    public String checkValidUsername(String username) {
        String message = "";
        if (username == null) {
            return "用户名不能为空";
        } else {
            if (!regexUtil.checkValidUsername(username)) message += "用户名需要为11位整数或者6位整数\n";

            User user = userRepository.findByUsername(username);
            if (user != null) message += "用户名已被注册，请更换\n";
            return message;
        }
    }

    public String checkValidPassword(String password) {
        String message = "";
        if (!regexUtil.checkValidString(password)) {
            message += "密码需要由字母、数字、-、_构成\n";
        }
        int count = 0;
        if (regexUtil.containLetter(password)) count++;
        if (regexUtil.containNumber(password)) count++;
        if (regexUtil.containSpecial(password)) count++;
        if (count < 2) message += "密码需要包含字母、数字、(-、_)中的至少两种\n";
        if (!regexUtil.checkValidLength(password)) message += "密码长度需在6-32个字符\n";
        return message;
    }

    public String checkValidEmail(String email) {
        String message = "";
        if (!regexUtil.checkValidEmail(email)) {
            message = "邮箱格式非法(此处用雷·汤姆林森创立的标准E-mail格式)";
        }
        return message;
    }
}
