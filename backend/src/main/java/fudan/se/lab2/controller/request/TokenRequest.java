package fudan.se.lab2.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenRequest {
    @JsonProperty(value = "token")
    private int token;

    public int getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "TokenRequest{" +
                "token=" + token +
                '}';
    }

    public void setToken(int token) {
        this.token = token;
    }
}
