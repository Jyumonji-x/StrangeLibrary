package fudan.se.lab2.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenDuplicateRequest {
    @JsonProperty(value = "token")
    private int token;

    @JsonProperty(value = "duplicate_id")
    private String duplicate_id;

    public TokenDuplicateRequest() {

    }

    public TokenDuplicateRequest(int token, String duplicate_id) {
        this.token = token;
        this.duplicate_id = duplicate_id;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public String getDuplicate_id() {
        return duplicate_id;
    }

    public void setDuplicate_id(String duplicate_id) {
        this.duplicate_id = duplicate_id;
    }
}
