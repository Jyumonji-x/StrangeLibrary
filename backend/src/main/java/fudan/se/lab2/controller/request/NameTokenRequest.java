package fudan.se.lab2.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NameTokenRequest {
    @JsonProperty(value = "user_name")
    private String userName;

    @JsonProperty(value = "token")
    private int token;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "NameTokenRequest{" +
                "userName='" + userName + '\'' +
                ", token=" + token +
                '}';
    }
}
