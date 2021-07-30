package fudan.se.lab2.util;

//import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {


    String regex = "";

    public RegexUtil() {
    }

    public RegexUtil(String regex) {
        this.regex = regex;
    }

    public boolean regexCheck(String string) {
        return Pattern.matches(regex, string);
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    //是否由字母、数字、-、_构成
    public boolean checkValidString(String str) {
        String regexValidString = "^[\\w-_]*$";
        return Pattern.matches(regexValidString, str);
    }

    //是否由字母或-开头
    public boolean checkValidHead(String str) {
        String regexValidHead = "^[-A-Za-z](.*?)";
        return Pattern.matches(regexValidHead, str);
    }

    //长度是否在6-32个字符
    public boolean checkValidLength(String str) {
        String regexValidLength = "^[\\w-_]{6,32}$";
        return Pattern.matches(regexValidLength, str);
    }

    //是否包含字母
    public boolean containLetter(String str) {
        String regexContainLetter = ".*[A-Za-z]+.*";
        return Pattern.matches(regexContainLetter, str);
    }

    //是否包含-_
    public boolean containSpecial(String str) {
        String regexContainSpecial = ".*[-_]+.*";
        return Pattern.matches(regexContainSpecial, str);
    }

    //是否包含数字
    public boolean containNumber(String str) {
        String regexContainNumber = ".*[0-9]+.*";
        return Pattern.matches(regexContainNumber, str);
    }

    //邮箱格式是否合法(此处用雷·汤姆林森创立的标准E-mail格式)
    public boolean checkValidEmail(String str) {
        String regexValidEmail = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        return Pattern.matches(regexValidEmail, str);
    }

    //是否为学工号格式
    public boolean checkValidUsername(String str){
        String regexValidUsername = "(\\d{11}|\\d{6})$";
        return Pattern.matches(regexValidUsername, str);
    }
}
