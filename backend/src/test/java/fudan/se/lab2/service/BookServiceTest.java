package fudan.se.lab2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fudan.se.lab2.controller.request.BookBrowseRequest;
import fudan.se.lab2.controller.request.BookTitleRequest;
import fudan.se.lab2.controller.request.ISBNRequest;
import fudan.se.lab2.domain.Book;
import fudan.se.lab2.domain.BookDuplicate;
import fudan.se.lab2.domain.enhanced.BookWithNumber;
import fudan.se.lab2.repository.DuplicateRepository;
import fudan.se.lab2.util.JsonMap;
import fudan.se.lab2.util.TypeCaster;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class BookServiceTest {
    @Autowired
    private BookService bookService;

    @Autowired
    private DuplicateRepository duplicateRepository;

    MockMultipartFile mockMultipartFile = new MockMultipartFile("mock", "mock.jpg", "image", new byte[]{1, 2});


    @Test
    void upload() throws IOException {
        JsonMap result = bookService.upload("玩具修理者", "小林泰三", "测试用简介", 9784048729529L,
                new Date(), mockMultipartFile, 40.00);
        assertEquals(1, result.get("rtn"));
        assertEquals("上传成功", result.get("message"));
    }

    @Test
    void searchBookByFullTitle() throws IOException {
        bookService.upload("玩具修理者", "小林泰三", "测试用简介", 9784048729529L,
                new Date(), mockMultipartFile, 40.00);
        Object result = bookService.searchBookByTitle(new BookTitleRequest("玩具修理者")).get("books");
        List<BookWithNumber> books = TypeCaster.castList(result, BookWithNumber.class);
        assertEquals("玩具修理者", books.get(0).getTitle());
    }

    @Test
    void searchBookByFuzzyTitle() throws IOException {
        bookService.upload("玩具修理者", "小林泰三", "测试用简介", 9784048729529L,
                new Date(), mockMultipartFile, 40.00);
        bookService.upload("凡人修仙传", "我不认识", "测试用简介", 1111111111L,
                new Date(), mockMultipartFile, 40.00);
        Object result = bookService.searchBookByTitle(new BookTitleRequest("修")).get("books");
        List<BookWithNumber> books = TypeCaster.castList(result, BookWithNumber.class);
        assertEquals(2, books.size());
    }

    @Test
    void getBookByISBN() throws IOException {
        bookService.upload("玩具修理者", "小林泰三", "测试用简介", 9784048729529L,
                new Date(), mockMultipartFile, 40.00);
        Book book = (Book) bookService.getBookByISBN(new ISBNRequest(9784048729529L)).get("book");
        assertEquals("玩具修理者", book.getTitle());
    }

    @Test
    void browse() {
        BookBrowseRequest request = new BookBrowseRequest();
        request.setPage(1);
        request.setSize(8);
        JsonMap map = bookService.browse(request);
        assertEquals("浏览书库成功", map.get("message"));
    }

    @Test
    void browseAndCopeData() {
        BookDuplicate bookDuplicate = new BookDuplicate();
        bookDuplicate.setTimeValid(new Date());
        bookDuplicate.setBranch("邯郸");
        bookDuplicate.setStatus("预约");
        bookDuplicate.setDuplicateId("1111111111-001");
        bookDuplicate.setBorrower("19302010020");
        bookDuplicate.setTitle("兄弟");
        duplicateRepository.save(bookDuplicate);
        assertEquals(1, duplicateRepository.findBookDuplicatesByStatus("预约").size());
        BookBrowseRequest request = new BookBrowseRequest();
        request.setPage(1);
        request.setSize(8);
        JsonMap map = bookService.browse(request);
        assertEquals("浏览书库成功", map.get("message"));
        assertEquals(0, duplicateRepository.findBookDuplicatesByStatus("预约").size());
    }
}
