package fudan.se.lab2.domain.enhanced;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fudan.se.lab2.domain.Book;
import fudan.se.lab2.domain.utils.JsonDateSerializer;

import javax.persistence.Column;
import java.util.Date;

public class BookWithNumber extends Book {
    public BookWithNumber(Book book) {
        this.author = book.getAuthor();
        this.title = book.getTitle();
        this.cover = book.getCover();
        this.time_publish = book.getTime_publication();
        this.time_create = book.getTime_create();
        this.ISBN = book.getISBN();
        this.intro = book.getIntro();
    }

    private int duplicate_number = 0;
    private int available_number = 0;
    private String title;
    private String author;
    private String intro;
    private long ISBN;
    private String cover;
    private Date time_publish;
    private Date time_create;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public long getISBN() {
        return ISBN;
    }

    public void setISBN(long ISBN) {
        this.ISBN = ISBN;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getTime_publication() {
        return time_publish;
    }

    public void setTime_publication(Date time_publish) {
        this.time_publish = time_publish;
    }

    public Date getTime_create() {
        return time_create;
    }

    public void setTime_create(Date time_create) {
        this.time_create = time_create;
    }

    public int getDuplicate_number() {
        return duplicate_number;
    }

    public void setDuplicate_number(int duplicate_number) {
        this.duplicate_number = duplicate_number;
    }

    public int getAvailable_number() {
        return available_number;
    }

    public void setAvailable_number(int available_number) {
        this.available_number = available_number;
    }
}
