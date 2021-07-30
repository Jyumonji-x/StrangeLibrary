package fudan.se.lab2.controller.request;


import java.util.Set;

/**
 * @author Tongqiao Xu, ZhangMing
 */
public class UserModifyRequest {
    private Long user_id;
    private String old_password;

    private String password;
    private String user_name;
    private String email;


    public UserModifyRequest() {}

    public Long getUser_id() { return user_id;}

    public void setUser_id(Long user_id) {this.user_id = user_id;}

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

    public String getOld_password() {
        return this.old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    @Override
    public String toString() {
        return "UserModifyRequest{" +
                "user_id=" + user_id +
                ", old_password='" + this.old_password + '\'' +
                ", user_name='" + user_name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
