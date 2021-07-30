package se24.bookservice.service;

import se24.bookservice.domain.Book;
import se24.bookservice.domain.pack.BookPack;
import se24.bookservice.repository.BookRepository;
//import fudan.se.lab2.repository.BorrowRepository;
//import fudan.se.lab2.repository.DuplicateRepository;
//import fudan.se.lab2.repository.SubscribeOverdueRepository;
import se24.bookservice.repository.CopyRepository;
import se24.bookservice.util.ReturnMap;
import se24.bookservice.util.UploadChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author WangHaiWei, ZhangMing,YuanYiCong
 */
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final CopyRepository copyRepository;
    private final UploadChecker uploadChecker = new UploadChecker();

    @Autowired
    public BookService(BookRepository bookRepository,
                       CopyRepository copyRepository) {
        this.bookRepository = bookRepository;
        this.copyRepository = copyRepository;
    }

    public ReturnMap upload(String title, String author, String intro,
                            String ISBN, Date time_publish, MultipartFile cover, double price) throws IOException {
        ReturnMap map = new ReturnMap();
        int rtn = 0;
        String message = "";
        message += uploadChecker.uploadCheck(title, author, intro, ISBN, time_publish, cover);

        if (message.equals("") && bookRepository.findBookByISBN(ISBN) != null) {
            message += "图书ISBN号重复";
        }

        // store the cover image
        if (message.equals("")) {
            String coverName = cover.getOriginalFilename();  // 完整文件名
            assert coverName != null;
            String suffixName = coverName.substring(coverName.lastIndexOf('.')); // 后缀
            coverName = UUID.randomUUID() + suffixName; // 生成新文件名，添加后缀

            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!path.exists()) {
                path = new File("");
            }
            System.out.println(path.getAbsolutePath() + "/static/img/" + coverName);
            File upload = new File(path.getAbsolutePath(), "static/img/" + coverName);
            if (!upload.getParentFile().exists()) {
                upload.getParentFile().mkdirs();
            }
            cover.transferTo(upload);

            // store the book information
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setIntro(intro);
            book.setISBN(ISBN);
            book.setCover(coverName);
            book.setTime_publish(time_publish);
            book.setTime_create(new Date());
            book.setPrice(price);
            bookRepository.save(book);
            rtn = 1;
            message = "上传成功";
        }
        map.setRtn(rtn);
        map.setMessage(message);
        return map;
    }

    //许同樵 前端debug 修改
    public ReturnMap browse() {
        System.out.println("BookService.browse called");
        ReturnMap map = new ReturnMap();
        int rtn = 1;
        String message = "浏览书库成功";
        List<Book> books = bookRepository.findAll();
        if (books == null) {
            books = new ArrayList<>();
        }
        List<BookPack> bookPacks = new ArrayList<>();
        for (Book book : books
        ) {
            BookPack bookPack = new BookPack(book);
            String isbn = book.getISBN();
            int copyNum = copyRepository.countCopiesByISBN(isbn);
            int availableNum = copyRepository.countCopiesByISBNAndStatus(isbn, "在库");
            bookPack.setAvailable_number(availableNum);
            bookPack.setCopy_number(copyNum);
            bookPacks.add(bookPack);
        }
        map.setRtn(rtn);
        map.setMessage(message);
        map.put("books", bookPacks);
        return map;
    }

    public ReturnMap getBookByISBN(String isbn) {
        System.out.println("BookService.getBook called");
        ReturnMap map = new ReturnMap();
        Book book = bookRepository.findBookByISBN(isbn);
        if (book == null) {
            map.setMessage("没有找到对应书籍");
            map.setRtn(0);
        } else {
            BookPack bookPack = new BookPack(book);
            String aIsbn = book.getISBN();
            int copyNum = copyRepository.countCopiesByISBN(aIsbn);
            int availableNum = copyRepository.countCopiesByISBNAndStatus(aIsbn, "在库");
            bookPack.setAvailable_number(availableNum);
            bookPack.setCopy_number(copyNum);

            map.setMessage("获取书籍成功");
            map.setRtn(1);
            map.put("book", bookPack);
        }
        return map;
    }

    public ReturnMap searchBookByTitle(String title) {
        System.out.println("BookService.searchBookByTitle called");
        ReturnMap map = new ReturnMap();
        int rtn = 1;
        String message = "搜索成功";
        boolean flag = true;
        if (title == null) {
            rtn = 0;
            message = "搜索标题为null 搜索失败";
            flag = false;
        }
        if (flag) {
            List<Book> books = bookRepository.findBooksByTitleLike("%" + title + "%");
            List<BookPack> bookPacks = new ArrayList<>();
            for (Book book : books
            ) {
                BookPack bookPack = new BookPack(book);
                String isbn = book.getISBN();
                int copyNum = copyRepository.countCopiesByISBN(isbn);
                int availableNum = copyRepository.countCopiesByISBNAndStatus(isbn, "在库");
                bookPack.setAvailable_number(availableNum);
                bookPack.setCopy_number(copyNum);
                bookPacks.add(bookPack);
            }
            map.put("books", bookPacks);
        }
        map.setRtn(rtn);
        map.setMessage(message);
        return map;
    }
}

