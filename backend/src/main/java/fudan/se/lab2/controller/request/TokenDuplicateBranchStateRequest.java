package fudan.se.lab2.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenDuplicateBranchStateRequest {
    @JsonProperty(value = "token")
    private int token;

    @JsonProperty(value = "duplicate_id")
    private String duplicate_id;

    @JsonProperty(value = "branch")
    private String branch;

    @JsonProperty(value = "status")
    private String status;//完好，遗失，损坏


    public TokenDuplicateBranchStateRequest() {
    }

    public TokenDuplicateBranchStateRequest(int token, String duplicate_id, String branch, String status) {
        this.token = token;
        this.duplicate_id = duplicate_id;
        this.branch = branch;
        this.status = status;
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

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
