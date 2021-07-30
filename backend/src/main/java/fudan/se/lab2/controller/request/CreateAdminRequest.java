package fudan.se.lab2.controller.request;

/**
 * @author WangHaiWei
 */
public class CreateAdminRequest {
    private int token;
    private String username;
    private String password;
    private String email;

    public CreateAdminRequest() {}

    public CreateAdminRequest(int token, String username, String password, String email) {
        this.token = token;
        this.username = username;
        this.password = password;
        this.email = email;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }
}
