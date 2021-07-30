package fudan.se.lab2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fudan.se.lab2.controller.request.*;
import fudan.se.lab2.domain.FineRecord;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.repository.FineRepository;
import fudan.se.lab2.store.MyToken;
import fudan.se.lab2.repository.BorrowRepository;
import fudan.se.lab2.repository.DuplicateRepository;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.util.JsonMap;
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

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class BorrowServiceTest {
    @Autowired
    private BorrowService borrowService;
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
    @Autowired
    private FineRepository fineRepository;

    MockMultipartFile mockMultipartFile = new MockMultipartFile("mock", "mock.jpg", "image", new byte[]{1, 2});
    long ISBN = 9784048729529L;
    String ISBNStr = "9784048729529-001";

    int getAdminToken() throws JsonProcessingException {
        MyToken.init();
        //整个管理员用户
        if (userRepository.findByUsername("19302010000") == null) {
            User admin = new User("19302010000", "abcd1234", "19302010000@fudan.edu.cn", 1, new Date(), new Date(), "");
            userRepository.saveAndFlush(admin);
        }
        JsonMap loginReturn = userService.login(new LoginRequest("19302010000", "abcd1234"));
        return (int) loginReturn.get("token");
    }

    void initData() throws IOException {
        //整个书籍信息
        bookService.upload("玩具修理者", "小林泰三", "测试用简介", ISBN,
                new Date(), mockMultipartFile, 40.00);
        //整点图书副本
        BookDuplicateAddRequest request = new BookDuplicateAddRequest(getAdminToken(), ISBN, "邯郸", 3);
        duplicateService.add(request);

        //整个超管用户
        if (userRepository.findByUsername("admin") == null) {
            User admin = new User("admin", "admin", "19302010020@fudan.edu.cn", 2, new Date(), new Date(), "");
            userRepository.saveAndFlush(admin);
        }
    }

    @Test
    void subscribeDuplicate() throws IOException {
        initData();
        assertEquals("在库", duplicateRepository.findBookDuplicateByDuplicateId(ISBNStr).getStatus());
        User user = new User("19302010020", "", "", 0, new Date(), new Date(), "本科生");
        userRepository.save(user);
        Integer userToken = MyToken.insertToken(user);
        TokenDuplicateRequest tokenDuplicateRequest = new TokenDuplicateRequest(userToken, ISBNStr);
        JsonMap jsonMap = borrowService.subscribeBook(tokenDuplicateRequest);
        assertEquals(1, jsonMap.get("rtn"));
        assertEquals("预约成功", jsonMap.get("message"));
    }

    @Test
    void prohibitAdminSubscribe() throws IOException {
        initData();
        assertEquals("在库", duplicateRepository.findBookDuplicateByDuplicateId(ISBNStr).getStatus());
        TokenDuplicateRequest tokenDuplicateRequest = new TokenDuplicateRequest(getAdminToken(), ISBNStr);
        JsonMap jsonMap = borrowService.subscribeBook(tokenDuplicateRequest);
        assertEquals(0, jsonMap.get("rtn"));
        assertEquals("管理员无法预约", jsonMap.get("message"));
    }

    @Test
    void getSubscribedDuplicate() throws IOException {
        // 19302010020用户预约了ISBN-001这本书
        initData();
        User user = new User("19302010020", "", "", 0, new Date(), new Date(), "本科生");
        userRepository.saveAndFlush(user);
        Integer userToken = MyToken.insertToken(user);
        TokenDuplicateRequest tokenDuplicateRequest = new TokenDuplicateRequest(userToken, ISBNStr);
        borrowService.subscribeBook(tokenDuplicateRequest);

        // 由管理员进行现场取书
        String branch = "邯郸";
        NameTokenDuplicateBranchRequest request = new NameTokenDuplicateBranchRequest("19302010020", getAdminToken(), ISBNStr, branch);
        JsonMap map = borrowService.getSubscribeBook(request);
        assertEquals(1, map.get("rtn"));
        System.out.println(map.toJson());
    }

    @Test
    void borrowDuplicate() throws IOException {
        initData();
        User user = new User("19302010020", "", "", 0, new Date(), new Date(), "本科生");
        userRepository.save(user);

        // 现场借书
        String branch = "邯郸";
        NameTokenDuplicateBranchRequest request = new NameTokenDuplicateBranchRequest("19302010020", getAdminToken(), ISBNStr, branch);
        assertEquals(1, borrowService.borrowBook(request).get("rtn"));
    }

    @Test
    void borrowDuplicateWithFine() throws IOException {
        initData();
        User user = new User("19302010020", "", "", 0, new Date(), new Date(), "本科生");
        userRepository.save(user);
        FineRecord fineRecord = new FineRecord(10);
        fineRecord.setUsername("19302010020");
        fineRepository.save(fineRecord);

        // 现场借书
        String branch = "邯郸";
        NameTokenDuplicateBranchRequest request = new NameTokenDuplicateBranchRequest("19302010020", getAdminToken(), ISBNStr, branch);
        JsonMap map = borrowService.borrowBook(request);
        assertEquals(0, map.get("rtn"));
        assertEquals("请先付清罚款再预约", map.get("message"));
    }

    @Test
    void returnDuplicate() throws IOException {
        initData();
        User user = new User("19302010020", "", "", 0, new Date(), new Date(), "本科生");
        userRepository.save(user);
        int adminToken = getAdminToken();
        // 现场借书
        String outBranch = "邯郸";
        NameTokenDuplicateBranchRequest request = new NameTokenDuplicateBranchRequest("19302010020", adminToken, ISBNStr, outBranch);
        borrowService.borrowBook(request);
        // 然后还掉
        String rtnBranch = "枫林";
        TokenDuplicateBranchStateRequest rtnRequest = new TokenDuplicateBranchStateRequest(adminToken, ISBNStr, rtnBranch, "完好");
        borrowService.returnBook(rtnRequest);
        assertEquals(rtnBranch, duplicateRepository.findBookDuplicateByDuplicateId(ISBNStr).getBranch());
        assertEquals("在库", duplicateRepository.findBookDuplicateByDuplicateId(ISBNStr).getStatus());
    }

    @Test
    void getBorrowInfo() throws IOException {
        initData();
        int adminToken = getAdminToken();
        // 现场借书
        String outBranch = "邯郸";
        NameTokenDuplicateBranchRequest request = new NameTokenDuplicateBranchRequest("19302010000", adminToken, ISBNStr, outBranch);
        borrowService.borrowBook(request);
        System.out.println(borrowService.borrowed("19302010000", adminToken));

        // 然后还掉
        String rtnBranch = "枫林";
        TokenDuplicateBranchStateRequest rtnRequest = new TokenDuplicateBranchStateRequest(adminToken, ISBNStr, rtnBranch, "完好");
        borrowService.returnBook(rtnRequest);
        System.out.println(borrowService.borrowed("19302010000", adminToken));
    }

    @Test
    void getHistory() {
        JsonMap map = borrowService.getHistory();
        assertEquals(1, map.get("rtn"));
        assertEquals("查询成功", map.get("message"));
    }

    @Test
    void getHistoryByUsername() {
        JsonMap map = borrowService.getHistoryByUsername("19302010020");
        assertEquals(1, map.get("rtn"));
        assertEquals("查询成功", map.get("message"));
    }

    @Test
    void getHistoryByDuplicateId() {
        JsonMap map = borrowService.getHistoryByUsername("9784048729529-001");
        assertEquals(1, map.get("rtn"));
        assertEquals("查询成功", map.get("message"));
    }


}
