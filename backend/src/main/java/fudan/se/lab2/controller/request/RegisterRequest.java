package fudan.se.lab2.controller.request;

import java.util.Set;

/**
 * @author ZhangMing
 */
public class RegisterRequest {
    private String user_name;
    private String password;
    private String email;
    private int captcha;

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "user_name='" + user_name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", captcha=" + captcha +
                '}';
    }

    public RegisterRequest() {
    }

    public RegisterRequest(String user_name, String email, String password, int captcha) {
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.captcha = captcha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCaptcha() {
        return captcha;
    }

    public void setCaptcha(int captcha) {
        this.captcha = captcha;
    }
}

