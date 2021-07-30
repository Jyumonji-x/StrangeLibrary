package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.BookDuplicateAddRequest;
import fudan.se.lab2.domain.Book;
import fudan.se.lab2.domain.BookDuplicate;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.store.MyToken;
import fudan.se.lab2.repository.BookRepository;
import fudan.se.lab2.repository.DuplicateRepository;
import fudan.se.lab2.util.DuplicateChecker;
import fudan.se.lab2.util.JsonMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

@Service
public class DuplicateService {
    private final DuplicateRepository duplicateRepository;
    private final BookRepository bookRepository;
    private final DuplicateChecker duplicateChecker = new DuplicateChecker();

    @Autowired
    public DuplicateService(DuplicateRepository duplicateRepository, BookRepository bookRepository) {
        this.duplicateRepository = duplicateRepository;
        this.bookRepository = bookRepository;
    }

    public JsonMap add(BookDuplicateAddRequest request) {
        System.out.println("DuplicateService.add called");
        JsonMap resultMap = new JsonMap();
        long ISBN = request.getISBN();
        String location = request.getLocation();
        int number = request.getNumber();
        int token = request.getToken();
        int rtn = 0;
        String message = "";

        User user = MyToken.getUserByTokenId(token);
        message = duplicateChecker.addCheck(user,ISBN,location);
        Book book = null;
        if (message.equals("")) {
            // 检查书籍信息是否存在
            book = bookRepository.findBookByISBN(ISBN);
            if (book == null) {
                message = "副本入库失败，书籍信息不存在";
            }
        }
        // 存储副本
        if (message.equals("")) {
            for (int i = 0; i < number; i++) {
                int count = duplicateRepository.countByISBN(ISBN);
                DecimalFormat decimalFormat = new DecimalFormat("000");
                String duplicate_id = ISBN + "-" + decimalFormat.format(count + 1L);
                BookDuplicate bookDuplicate = new BookDuplicate(book.getTitle(), ISBN, duplicate_id, new Date(), location);
                duplicateRepository.save(bookDuplicate);
            }
            rtn = 1;
            message = "副本入库成功";
        }
        resultMap.put("rtn", rtn);
        resultMap.put("message", message);
        return resultMap;
    }

    public JsonMap searchByISBN(long ISBN) {
        System.out.println("DuplicateService.searchByISBN() called");
        System.out.println("ISBN = " + ISBN);
        JsonMap resultMap = new JsonMap();
        int rtn = 0;
        String message = "";
        if (ISBN == 0) {
            message = "ISBN不能为空";
        } else {
            List<BookDuplicate> bookDuplicates = duplicateRepository.findBookDuplicatesByISBN(ISBN);
            resultMap.put("bookDuplicate", bookDuplicates);
            rtn = 1;
            message = "查询副本成功！";
        }
        resultMap.put("rtn", rtn);
        resultMap.put("message", message);
        return resultMap;
    }
}
