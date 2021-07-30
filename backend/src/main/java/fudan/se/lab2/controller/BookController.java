package fudan.se.lab2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fudan.se.lab2.controller.request.BookTitleRequest;
import fudan.se.lab2.controller.request.ISBNRequest;
import fudan.se.lab2.controller.request.BookBrowseRequest;
import fudan.se.lab2.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;

@RestController
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/api/book/upload")
    public ResponseEntity<?> uploadBook(
            @RequestParam(value = "title") String title,
            @RequestParam(value = "author") String author,
            @RequestParam(value = "intro") String intro,
            @RequestParam(value = "ISBN") long ISBN,
            @RequestParam(value = "time_publish") Date time_publish,
            @RequestParam(value = "cover") MultipartFile cover,
            @RequestParam(value = "price") double price
    ) throws IOException {
        return ResponseEntity.ok(bookService.upload(title, author, intro, ISBN, time_publish, cover,price).toJson());
    }

    @PostMapping("/api/book/browse")
    public ResponseEntity<?> browseBook(
            @RequestBody BookBrowseRequest request
    ) throws JsonProcessingException {
        System.out.print("BookController /api/book/browse called:");
        return ResponseEntity.ok(bookService.browse(request).toJson());
    }

    @PostMapping("/api/book/get_by_ISBN")
    public ResponseEntity<?> getBook(
            @RequestBody ISBNRequest request) throws JsonProcessingException {
        System.out.print("BookController /api/get_book called:");
        System.out.println("Param: ISBN = " + request.getISBN());
        return ResponseEntity.ok(bookService.getBookByISBN(request).toJson());
    }

    @PostMapping("/api/book/search_by_title")
    public ResponseEntity<?> searchBook(
            @RequestBody BookTitleRequest request) throws JsonProcessingException {
        System.out.print("BookController /api/search_by_title called:");
        System.out.println("Param: title = " + request.getTitle());
        return ResponseEntity.ok(bookService.searchBookByTitle(request).toJson());
    }
}
