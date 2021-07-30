package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.BookBrowseRequest;
import fudan.se.lab2.controller.request.BookTitleRequest;
import fudan.se.lab2.controller.request.ISBNRequest;
import fudan.se.lab2.domain.Book;
import fudan.se.lab2.domain.enhanced.BookWithNumber;
import fudan.se.lab2.repository.BookRepository;
import fudan.se.lab2.repository.BorrowRepository;
import fudan.se.lab2.repository.DuplicateRepository;
import fudan.se.lab2.repository.SubscribeOverdueRepository;
import fudan.se.lab2.util.JsonMap;
import fudan.se.lab2.util.UploadChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
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
    private final OverdueService overdueService;
    private final BookRepository bookRepository;
    private final DuplicateRepository duplicateRepository;
    private final BorrowRepository borrowRepository;
    private final SubscribeOverdueRepository subscribeOverdueRepository;
    private final UploadChecker uploadChecker = new UploadChecker();

    @Autowired
    public BookService(OverdueService overdueService, BookRepository bookRepository, DuplicateRepository duplicateRepository,
                       SubscribeOverdueRepository subscribeOverdueRepository, BorrowRepository borrowRepository) {
        this.overdueService = overdueService;
        this.bookRepository = bookRepository;
        this.duplicateRepository = duplicateRepository;
        this.subscribeOverdueRepository = subscribeOverdueRepository;
        this.borrowRepository = borrowRepository;
    }

    public JsonMap upload(String title, String author, String intro,
                          long ISBN, Date time_publish, MultipartFile cover, double price) throws IOException {
        System.out.println("BookService.upload() called");
        JsonMap resultMap = new JsonMap();
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
            Book book = new Book(title, author, intro, ISBN, coverName, time_publish, new Date(), price);
            bookRepository.save(book);
            rtn = 1;
            message = "上传成功";
        }
        resultMap.put("rtn", rtn);
        resultMap.put("message", message);
        return resultMap;
    }

    public JsonMap browse(BookBrowseRequest bookBrowseRequest) {
        System.out.println("BookService.browse called");

        //----------洗库，检查预约过期--------------
        overdueService.subscribeOverdueCheck();

        JsonMap resultMap = new JsonMap();
        int rtn = 1;
        String message = "浏览书库成功";
        int page = bookBrowseRequest.getPage();
        int size = bookBrowseRequest.getSize();
        Pageable pageRequest = PageRequest.of(page - 1, size);
        List<Book> books = bookRepository.findAll(pageRequest).toList();
        List<BookWithNumber> bookWithNumbers = new ArrayList<>();
        for (Book book : books
        ) {
            BookWithNumber bookWithNumber = new BookWithNumber(book);
            int duplicate_number = duplicateRepository.countBookDuplicatesByISBN(book.getISBN());
            int available_number = duplicateRepository.countBookDuplicatesByISBNAndStatus(book.getISBN(), "在库");
            bookWithNumber.setAvailable_number(available_number);
            bookWithNumber.setDuplicate_number(duplicate_number);
            bookWithNumbers.add(bookWithNumber);
        }
        long count = bookRepository.count();
        resultMap.put("rtn", rtn);
        resultMap.put("message", message);
        resultMap.put("count", count);
        resultMap.put("books", bookWithNumbers);
        System.out.println("count of books = " + count);


        return resultMap;
    }

    public JsonMap getBookByISBN(ISBNRequest request) {
        System.out.println("BookService.getBook called");
        long ISBN = request.getISBN();
        JsonMap resultMap = new JsonMap();
        Book book = bookRepository.findBookByISBN(ISBN);
        if (book == null) {
            resultMap.put("message", "没有找到对应书籍");
            resultMap.put("rtn", 0);
        } else {
            BookWithNumber bookWithNumber = new BookWithNumber(book);
            int duplicate_number = duplicateRepository.countBookDuplicatesByISBN(book.getISBN());
            int available_number = duplicateRepository.countBookDuplicatesByISBNAndStatus(book.getISBN(), "在库");
            bookWithNumber.setAvailable_number(available_number);
            bookWithNumber.setDuplicate_number(duplicate_number);

            resultMap.put("message", "获取书籍成功");
            resultMap.put("rtn", 1);
            resultMap.put("book", bookWithNumber);
        }
        return resultMap;
    }

    public JsonMap searchBookByTitle(BookTitleRequest request) {
        System.out.println("BookService.searchBookByTitle called");
        String title = request.getTitle();
        JsonMap resultMap = new JsonMap();
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
            List<BookWithNumber> bookWithNumbers = new ArrayList<>();
            for (Book book : books
            ) {
                BookWithNumber bookWithNumber = new BookWithNumber(book);
                int duplicate_number = duplicateRepository.countBookDuplicatesByISBN(book.getISBN());
                int available_number = duplicateRepository.countBookDuplicatesByISBNAndStatus(book.getISBN(), "在库");
                bookWithNumber.setAvailable_number(available_number);
                bookWithNumber.setDuplicate_number(duplicate_number);
                bookWithNumbers.add(bookWithNumber);
            }
            resultMap.put("books", bookWithNumbers);
        }
        resultMap.put("rtn", rtn);
        resultMap.put("message", message);
        return resultMap;
    }
}

