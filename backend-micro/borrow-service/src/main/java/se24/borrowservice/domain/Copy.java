package se24.borrowservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Setter
@Getter
@ToString
public class Copy {
    private String copyId;

    private String title;
    private String branch;
    private String ISBN;

    private String status;
    private String borrower;

    private Date timeCreate;

    public Copy() {

    }
}
