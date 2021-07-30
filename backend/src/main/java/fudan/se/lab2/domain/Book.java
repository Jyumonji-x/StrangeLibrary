package fudan.se.lab2.domain;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fudan.se.lab2.domain.utils.JsonDateSerializer;

import javax.persistence.*;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * @author WangHaiWei , ZhangMing
 */
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    private String author;
    private String intro;

    @Column(unique = true)
    private long ISBN;
    private double price;
    private String cover;
    private Date time_publish;
    private Date time_create;

    public Book() {
    }

    public Book(String title, String author, String intro, long ISBN, String cover, Date time_publish, Date time_create,
                double price) {
        this.title = title;
        this.author = author;
        this.intro = intro;
        this.ISBN = ISBN;
        this.cover = cover;
        this.time_publish = time_publish;
        this.time_create = time_create;
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
