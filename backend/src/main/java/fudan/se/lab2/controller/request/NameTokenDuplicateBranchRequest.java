package fudan.se.lab2.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NameTokenDuplicateBranchRequest {
    @JsonProperty(value = "borrow_name")
    private String borrow_name;

    @JsonProperty(value = "token")
    private int token;

    @JsonProperty(value = "duplicate_id")
    private String duplicate_id;

    @JsonProperty(value = "branch")
    private String branch;

    public NameTokenDuplicateBranchRequest() {
    }

    public NameTokenDuplicateBranchRequest(String borrow_name, int token, String duplicate_id, String branch) {
        this.borrow_name = borrow_name;
        this.token = token;
        this.duplicate_id = duplicate_id;
        this.branch = branch;
    }

    public String getDuplicate_id() {
        return duplicate_id;
    }

    public void setDuplicate_id(String duplicate_id) {
        this.duplicate_id = duplicate_id;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public String getBorrow_name() {
        return borrow_name;
    }

    public void setBorrow_name(String borrow_name) {
        this.borrow_name = borrow_name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
