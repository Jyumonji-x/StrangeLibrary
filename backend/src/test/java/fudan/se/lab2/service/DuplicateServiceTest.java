package fudan.se.lab2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fudan.se.lab2.controller.request.BookDuplicateAddRequest;
import fudan.se.lab2.controller.request.LoginRequest;
import fudan.se.lab2.domain.BookDuplicate;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.store.MyToken;
import fudan.se.lab2.repository.DuplicateRepository;
import fudan.se.lab2.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class DuplicateServiceTest {
    @Autowired
    private DuplicateService duplicateService;
    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DuplicateRepository duplicateRepository;

    MockMultipartFile mockMultipartFile = new MockMultipartFile("mock", "mock.jpg", "image", new byte[]{1, 2});

    int getAdminToken() throws JsonProcessingException {
        MyToken.init();
        //整个管理员用户
        if (userRepository.findByUsername("19302010000") == null) {
            User admin = new User("19302010000", "abcd1234", "19302010000@fudan.edu.cn", 2, new Date(), new Date(),"本科生");
            userRepository.save(admin);
        }
        JsonMap loginReturn = userService.login(new LoginRequest("19302010000", "abcd1234"));
        return (int) loginReturn.get("token");
    }

    @Test
    void addNumber() throws IOException {
        int adminToken = getAdminToken();
        long ISBN = 9784048729529L;
        int number = 4;
        //整个书籍信息
        System.out.println(new MyToken());
        bookService.upload("玩具修理者", "小林泰三", "测试用简介", ISBN, new Date(), mockMultipartFile, 40.00);
        BookDuplicateAddRequest request = new BookDuplicateAddRequest(adminToken, ISBN, "邯郸", number);

        //测试add方法
        System.out.println(duplicateService.add(request));
        assertEquals(number, duplicateRepository.countByISBN(ISBN));
    }

    @Test
    void searchByISBN() throws IOException {
        int adminToken = getAdminToken();
        long ISBN = 9784048729529L;
        int number = 4;
        //整个书籍信息
        bookService.upload("玩具修理者", "小林泰三", "测试用简介", ISBN, new Date(), mockMultipartFile, 40.00);
        BookDuplicateAddRequest request = new BookDuplicateAddRequest(adminToken, ISBN, "邯郸", number);
        //整几个副本
        System.out.println(duplicateService.add(request));
        System.out.println(duplicateService.searchByISBN(ISBN));
//        assertEquals(1, duplicateService.searchByISBN(ISBN).get("rtn"));
//        assertEquals(number, TypeCaster.castList(duplicateService.searchByISBN(ISBN).get("book_duplicate"), BookDuplicate.class).size());
    }
}
