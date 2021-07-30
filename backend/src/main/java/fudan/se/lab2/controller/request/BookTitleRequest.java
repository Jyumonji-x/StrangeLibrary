package fudan.se.lab2.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookTitleRequest {
    @JsonProperty(value = "title")
    private String title;

    public BookTitleRequest(){};

    public BookTitleRequest(String title){
        this.title=title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
