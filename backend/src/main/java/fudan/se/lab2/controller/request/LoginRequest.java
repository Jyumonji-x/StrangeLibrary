package fudan.se.lab2.controller.request;

/**
 * @author ZhangMing
 */
public class LoginRequest {
    private String user_name;
    private String password;

    public LoginRequest() {}

    @Override
    public String toString() {
        return "LoginRequest{" +
                "user_name='" + user_name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public LoginRequest(String user_name, String password){
        this.user_name=user_name;
        this.password=password;
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
}
