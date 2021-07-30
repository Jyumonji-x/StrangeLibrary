package fudan.se.lab2.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Tongqiao Xu
 */
public class BookDuplicateAddRequest {
    private int token;

    @JsonProperty(value = "ISBN")
    private long ISBN;
    private String location;
    private int number;

    public BookDuplicateAddRequest() {
    }

    public BookDuplicateAddRequest(int token, long ISBN, String location, int number) {
        this.ISBN = ISBN;
        this.location = location;
        this.number = number;
        this.token = token;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public long getISBN() {
        return ISBN;
    }

    public void setISBN(long ISBN) {
        this.ISBN = ISBN;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "BookDuplicateAddRequest{" +
                "token=" + token +
                ", ISBN=" + ISBN +
                ", location='" + location + '\'' +
                ", number=" + number +
                '}';
    }
}
