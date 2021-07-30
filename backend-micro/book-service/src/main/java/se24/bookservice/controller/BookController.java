package se24.bookservice.controller;

import org.hibernate.validator.constraints.ISBN;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import se24.bookservice.domain.Book;
import se24.bookservice.repository.BookRepository;
import se24.bookservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;

@RestController
public class BookController {
    private final BookService bookService;
    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    @RequestMapping(path="/img/{filepath}",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImg(
            @PathVariable String filepath
    ) throws IOException {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if (!path.exists()) {
            path = new File("");
        }
        File img = new File(path.getAbsolutePath(), "static/img/" + filepath);
        byte[] bytes = new byte[0];
        if (img.exists()) {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(img);
                bytes = new byte[inputStream.available()];
                inputStream.read(bytes, 0, inputStream.available());

            }catch (Exception e){
                System.out.println(e.getMessage());
            }finally {
                inputStream.close();
            }
        }
        return bytes;
    }


    @GetMapping("/api/book/price/{isbn}")
    public ResponseEntity<Double> getPrice(
            @PathVariable String isbn
    ) {
        Book book = bookRepository.findBookByISBN(isbn);
        return ResponseEntity.ok(book.getPrice());
    }

    @PostMapping("/api/book/upload")
    public ResponseEntity<?> uploadBook(
            @RequestParam(value = "title") String title,
            @RequestParam(value = "author") String author,
            @RequestParam(value = "intro") String intro,
            @RequestParam(value = "ISBN") String ISBN,
            @RequestParam(value = "time_publish") Date time_publish,
            @RequestParam(value = "cover") MultipartFile cover,
            @RequestParam(value = "price") double price
    ) throws IOException {
        return ResponseEntity.ok(bookService.upload(title, author, intro, ISBN, time_publish, cover, price).getMap());
    }

    //许同樵 前端debug 修改
    @PostMapping("/api/book/get/browse")
    public ResponseEntity<?> browseBook() {
        System.out.print("BookController /api/book/browse called:");
        return ResponseEntity.ok(bookService.browse().getMap());
    }

    @PostMapping("/api/book/get/ISBN/{ISBN}")
    public ResponseEntity<?> getBook(
            @PathVariable String ISBN) {
        System.out.print("BookController /api/get_book called:");
        return ResponseEntity.ok(bookService.getBookByISBN(ISBN).getMap());
    }


    @PostMapping("/api/book/get/title/{title}")
    public ResponseEntity<?> searchBook(
            @PathVariable String title) {
        System.out.print("BookController /api/search_by_title called:");
        return ResponseEntity.ok(bookService.searchBookByTitle(title).getMap());
    }
}
