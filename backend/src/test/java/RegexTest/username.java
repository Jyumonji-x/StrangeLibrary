package RegexTest;

import fudan.se.lab2.util.RegexUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class username {
    public static void main(String[] args) {
        username username = new username();
        username.testUserName("yicong222");

        System.out.println(username.testRgexUtil("yicong222"));
    }

    void testUserName(String username){
        int rtn = 1;
        String message = "注册成功";
        boolean flag = true;
        while(flag) {
            //用户名需要由字母、数字、-、_构成
            String regex = "^[\\w-_]*$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(username);
            if (!matcher.matches()) {
                rtn = 0;
                message = "用户名需要由字母、数字、-、_构成";
                flag = false;
            }
            //用户名需要由字母或-开头
            regex = "^[-A-Za-z](.*?)";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(username);
            if (!matcher.matches()) {
                rtn = 0;
                message = "用户名需要由字母或-开头";
                flag = false;
            }
            //用户名长度需在6-32个字符
            regex = "^[\\w-_]{6,32}$";
            pattern = Pattern.compile(regex);
            matcher = pattern.matcher(username);
            if (!matcher.matches()) {
                rtn = 0;
                message = "用户名长度需在6-32个字符";
                flag = false;
            }
            flag = false;
        }
        System.out.println(rtn + message);
    }

    boolean testRgexUtil(String str){
        RegexUtil regexUtil = new RegexUtil();
        regexUtil.setRegex("^[\\w-_]*$");
        return regexUtil.regexCheck(str);
    }
}
