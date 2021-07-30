package se24.bookservice.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ISBNRequest {
    @JsonProperty(value = "ISBN")
    private String ISBN;

    public ISBNRequest() {
    }
}
