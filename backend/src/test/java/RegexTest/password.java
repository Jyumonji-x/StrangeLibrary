package RegexTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class password {
    public static void main(String[] args) {
        password password = new password();
        password.testPassword("abcdaaa1_");
    }

    void testPassword(String password){
        //需要由字母、数字、-、_构成
        String regex = "^[\\w-_]*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        if(!matcher.matches()){
            System.out.println("需要由字母、数字、-、_构成");
        }

        //密码需要包含字母、数字、(-、_)中的至少两种
        int count = 0;
        regex = ".*[0-9]+.*";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(password);
        System.out.println(matcher.matches());

        if(matcher.matches()){count++;}


        regex = ".*[a-zA-Z]+.*";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(password);
        System.out.println(matcher.matches());

        if(matcher.matches()){count++;}

        regex = ".*[-_]+.*";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(password);
        System.out.println(matcher.matches());

        if(matcher.matches()){count++;}

        if(count < 2){
            System.out.println("密码需要包含字母、数字、(-、_)中的至少两种");
        }
    }
}
