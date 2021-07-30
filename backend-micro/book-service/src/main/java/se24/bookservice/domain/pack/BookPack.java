package se24.bookservice.domain.pack;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import se24.bookservice.domain.Book;

import java.util.Date;

@Setter
@Getter
@ToString
public class BookPack extends Book {
    public BookPack(Book book) {
        this.author = book.getAuthor();
        this.title = book.getTitle();
        this.cover = book.getCover();
        this.time_publish = book.getTime_publish();
        this.time_create = book.getTime_create();
        this.ISBN = book.getISBN();
        this.intro = book.getIntro();
        this.price = book.getPrice();
    }

    private int copy_number = 0;
    private int available_number = 0;
    private String title;
    private String author;
    private String intro;
    private String ISBN;
    private String cover;
    private double price;
    private Date time_publish;
    private Date time_create;
}
