package se24.bookservice;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;
import se24.bookservice.controller.request.BookBrowseRequest;
import se24.bookservice.controller.request.CopyAddRequest;
import se24.bookservice.controller.request.UserReserveRequest;
import se24.bookservice.domain.Book;
import se24.bookservice.domain.Copy;
import se24.bookservice.domain.User;
import se24.bookservice.repository.BookRepository;
import se24.bookservice.repository.CopyRepository;
import se24.bookservice.service.BookService;
import se24.bookservice.service.CopyService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class BookServiceApplicationTests {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CopyRepository copyRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BookService bookService;
    @InjectMocks
    private CopyService copyService;

    private final String mockTitle = "玩具修理者";
    private final String mockAuthor = "小林泰三";
    private final String mockIntro = "测试用简介";
    private final String mockISBN = "9784048729529";
    MockMultipartFile mockMultipartFile = new MockMultipartFile("mock", "mock.jpg", "image", new byte[]{1, 2});

    /**
     * upload
     **/
    @Test
    void uploadCorrectly() throws IOException {
        HashMap<String, Object> map = bookService.upload(mockTitle, mockAuthor,mockIntro, mockISBN,
                new Date(), mockMultipartFile, 40.00).getMap();
        assertEquals("上传成功", map.get("message"));
    }

    /**
     * getBookByISBN
     **/
    @Test
    void getBookByWrongISBN() {
        String isbn="1000000000";
        Mockito.when(bookRepository.findBookByISBN(isbn)).thenReturn(null);
        HashMap<String, Object> map = bookService.getBookByISBN(isbn).getMap();
        assertEquals("没有找到对应书籍",map.get("message"));
        System.out.println(bookService.getBookByISBN(isbn).getMap());
    }

    @Test
    void getBookByRightISBN(){
        String isbn="1000000000";
        Book mockBook = new Book();
        mockBook.setISBN(isbn);
        Mockito.when(bookRepository.findBookByISBN(isbn)).thenReturn(mockBook);
        Mockito.when(copyRepository.countCopiesByISBN(isbn)).thenReturn(5);
        Mockito.when(copyRepository.countCopiesByISBNAndStatus(isbn,"在库")).thenReturn(3);
        HashMap<String, Object> map = bookService.getBookByISBN(isbn).getMap();
        assertEquals("获取书籍成功",map.get("message"));
        System.out.println(bookService.getBookByISBN(isbn).getMap());
    }

    /**
     * searchBookByTitle
     **/
    @Test
    void searchBookWithoutTitle(){
        HashMap<String, Object> map = bookService.searchBookByTitle(null).getMap();
        assertEquals("搜索标题为null 搜索失败",map.get("message"));

    }

    @Test
    void searchBookByFullTitle(){
        //book1
        String title1 = "玩具修理者";
        String isbn1 = "10000000";
        Book mockBook1 = new Book();
        mockBook1.setISBN(isbn1);
        mockBook1.setTitle(title1);
        //book2
        String title2 = "凡人修仙传";
        String isbn2 = "66666666";
        Book mockBook2 = new Book();
        mockBook2.setISBN(isbn2);
        mockBook2.setTitle(title2);

        List<Book> mockBookList = new ArrayList<>();
        mockBookList.add(mockBook1);

        Mockito.when(bookRepository.findBooksByTitleLike("%" + title1 + "%")).thenReturn(mockBookList);
        Mockito.when(copyRepository.countCopiesByISBN(isbn1)).thenReturn(5);
        Mockito.when(copyRepository.countCopiesByISBNAndStatus(isbn1,"在库")).thenReturn(3);
        HashMap<String, Object> map = bookService.searchBookByTitle(title1).getMap();
        assertEquals("搜索成功",map.get("message"));
        System.out.println(bookService.searchBookByTitle(title1).getMap());

    }

    @Test
    void searchBookByFuzzyTitle(){
        //book1
        String title1 = "玩具修理者";
        String isbn1 = "10000000";
        Book mockBook1 = new Book();
        mockBook1.setISBN(isbn1);
        mockBook1.setTitle(title1);
        //book2
        String title2 = "凡人修仙传";
        String isbn2 = "66666666";
        Book mockBook2 = new Book();
        mockBook2.setISBN(isbn2);
        mockBook2.setTitle(title2);

        //target
        String target = "修";

        List<Book> mockBookList = new ArrayList<>();
        mockBookList.add(mockBook1);
        mockBookList.add(mockBook2);

        Mockito.when(bookRepository.findBooksByTitleLike("%" + target + "%")).thenReturn(mockBookList);
        Mockito.when(copyRepository.countCopiesByISBN(isbn1)).thenReturn(5);
        Mockito.when(copyRepository.countCopiesByISBN(isbn2)).thenReturn(10);
        Mockito.when(copyRepository.countCopiesByISBNAndStatus(isbn1,"在库")).thenReturn(3);  Mockito.when(copyRepository.countCopiesByISBN(isbn1)).thenReturn(5);
        Mockito.when(copyRepository.countCopiesByISBNAndStatus(isbn2,"在库")).thenReturn(4);
        HashMap<String, Object> map = bookService.searchBookByTitle(target).getMap();
        assertEquals("搜索成功",map.get("message"));
        System.out.println(bookService.searchBookByTitle(target).getMap());

    }

    /**
     *browse
     **/
    @Test
    void browseCorrectly(){
        BookBrowseRequest bookBrowseRequest = new BookBrowseRequest();
        int page = 1;
        int size = 8;
        bookBrowseRequest.setPage(page);
        bookBrowseRequest.setSize(size);

        List<Book> mockBookList = new ArrayList<>();
        Book book = new Book();
        String isbn = "1000000";
        book.setTitle("玩具修理者");
        book.setISBN(isbn);
        mockBookList.add(book);
        Mockito.when(copyRepository.countCopiesByISBN(isbn)).thenReturn(5);
        Mockito.when(copyRepository.countCopiesByISBNAndStatus(isbn,"在库")).thenReturn(3);
        Mockito.when(bookRepository.count()).thenReturn(1L);
        HashMap<String, Object> map = bookService.browse().getMap();
        assertEquals("浏览书库成功", map.get("message"));
        System.out.println(bookService.browse().getMap());

    }
    /**
     *add copy
     **/
    @Test
    void addCopyWithoutPermission(){
        CopyAddRequest copyAddRequest = new CopyAddRequest();
        User admin = new User();
        admin.setPermission("");
        ResponseEntity<User> responseEntity = ResponseEntity.ok(admin);

        System.out.println(ResponseEntity.ok(admin).getBody());
        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + copyAddRequest.getSession(), null, User.class))
                .thenReturn(responseEntity);

        HashMap<String, Object> map = copyService.add(copyAddRequest).getMap();
        assertEquals("创建管理员需要管理员权限",map.get("message"));
        System.out.println(copyService.add(copyAddRequest).getMap());

    }
    @Test
    void addCopyWithPermissionWithoutBook(){
        CopyAddRequest copyAddRequest = new CopyAddRequest();
        User admin = new User();
        admin.setPermission("超级管理员");
        ResponseEntity<User> responseEntity = ResponseEntity.ok(admin);

        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + copyAddRequest.getSession(), null, User.class))
                .thenReturn(responseEntity);

        Mockito.when(bookRepository.findBookByISBN(mockISBN)).thenReturn(null);
        HashMap<String, Object> map = copyService.add(copyAddRequest).getMap();
        assertEquals("添加失败，没有对应的书籍信息",map.get("message"));
        System.out.println(copyService.add(copyAddRequest).getMap());

    }
    @Test
    void doAdd(){
        User admin = new User();
        admin.setPermission("超级管理员");
        Book book = new Book();
        book.setISBN(mockISBN);
        book.setTitle(mockTitle);
        book.setAuthor(mockAuthor);
        book.setIntro(mockIntro);

        CopyAddRequest copyAddRequest = new CopyAddRequest();
        copyAddRequest.setNumber(3);

        ResponseEntity<User> responseEntity = ResponseEntity.ok(admin);
        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + copyAddRequest.getSession(), null, User.class))
                .thenReturn(responseEntity);
        Mockito.when(bookRepository.findBookByISBN(null)).thenReturn(book);
        Mockito.when(copyRepository.countCopiesByISBN(null)).thenReturn(3);

        HashMap<String, Object> map = copyService.add(copyAddRequest).getMap();
        assertEquals("副本入库成功,新增3本《玩具修理者》",map.get("message"));
        System.out.println(copyService.add(copyAddRequest).getMap());
    }

    /**
     *get copy
     **/

    @Test
    void getBookCopies(){
        String isbn = mockISBN;
        List<Copy> copies = new ArrayList<>();
        Copy copy = new Copy();
        copy.setTitle(mockTitle);
        copy.setISBN(mockISBN);
        copies.add(copy);

        Mockito.when(copyRepository.findCopiesByISBN(isbn)).thenReturn(copies);
        HashMap<String, Object> map = copyService.getBookCopies(isbn).getMap();
        assertEquals("查询副本成功",map.get("message"));
        System.out.println(copyService.getBookCopies(mockISBN).getMap());

    }

    /**
     *get user reversed
     **/

    @Test
    void getUserReversed(){

        UserReserveRequest request = new UserReserveRequest();
        List<Copy> copies = new ArrayList<>();
        Copy copy = new Copy();
        copy.setTitle(mockTitle);
        copy.setISBN(mockISBN);
        copies.add(copy);

        Mockito.when(copyRepository.findCopiesByStatusAndBorrowerAndBranch("预约",request.getUsername(),request.getBranch())).thenReturn(copies);
        HashMap<String, Object> map = copyService.getUserReserved(request).getMap();
        assertEquals("查询副本成功",map.get("message"));

    }



}
