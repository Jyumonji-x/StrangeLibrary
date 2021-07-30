package se24.bookservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Setter
@Getter
@ToString
public class Copy {
    @Id
    @Column(length = 20)
    private String copyId;

    private String title;
    private String branch;
    private String ISBN;

    private String status;
    private String borrower;

    private Date timeCreate;

    public Copy(Book book) {
        this.title = book.getTitle();
        this.ISBN = book.getISBN();
        this.status = "在库";
        this.timeCreate = new Date();
    }

    public Copy() {

    }
}
