package fudan.se.lab2.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ISBNRequest {
    @JsonProperty(value = "ISBN")
    private long ISBN;

    public ISBNRequest() {
    }

    public ISBNRequest(long ISBN) {
        this.ISBN = ISBN;
    }

    public long getISBN() {
        return ISBN;
    }

    public void setISBN(long ISBN) {
        this.ISBN = ISBN;
    }
}
