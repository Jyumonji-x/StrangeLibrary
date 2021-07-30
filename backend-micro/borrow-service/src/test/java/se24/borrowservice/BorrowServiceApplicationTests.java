package se24.borrowservice;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import se24.borrowservice.controller.request.BorrowRequest;
import se24.borrowservice.controller.request.ReserveRequest;
import se24.borrowservice.controller.request.ReturnRequest;
import se24.borrowservice.domain.Borrow;
import se24.borrowservice.domain.Copy;
import se24.borrowservice.domain.Rule;
import se24.borrowservice.domain.User;
import se24.borrowservice.repository.BorrowRepository;
import se24.borrowservice.repository.RuleRepository;
import se24.borrowservice.service.BorrowService;
import se24.borrowservice.service.RuleService;

import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class BorrowServiceApplicationTests {
    @Mock
    private BorrowRepository borrowRepository;
    @Mock
    private RuleRepository ruleRepository;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private BorrowService borrowService;
    @InjectMocks
    private RuleService ruleService;

    private final String mockUsername = "zmgg";
    private final String mockSession = "88888";
    private final String mockCopyId = "113514";
    private final String mockIdentity = "本科";
    private final String mockTitle = "软件工程";
    private final String mockISBN = "1122";
    /**
     * borrow
     */

    @Test
    void  borrowWithoutPermission() {
        BorrowRequest request = new BorrowRequest();
        request.setSession(mockSession);
        request.setCopyId(mockCopyId);
        User user = new User();
        user.setPermission("");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);
        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        HashMap<String, Object> map = borrowService.borrow(request).getMap();
        assertEquals("借书需要管理员权限",map.get("message"));
        System.out.println(map);
    }

    @Test
    void  borrowUnavailableBook() {
        BorrowRequest request = new BorrowRequest();
        request.setSession(mockSession);
        request.setCopyId(mockCopyId);

        User user = new User();
        user.setPermission("管理员");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);

        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        Mockito.when(borrowRepository.existsByCopyId(mockCopyId)).thenReturn(true); //书本在外

        HashMap<String, Object> map = borrowService.borrow(request).getMap();
        assertEquals("只能借阅在库副本",map.get("message"));
        System.out.println(map);

    }

    @Test
    void  borrowUnavailableCopy() {
        BorrowRequest request = new BorrowRequest();
        request.setSession(mockSession);
        request.setCopyId(mockCopyId);

        User user = new User();
        user.setPermission("管理员");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);

        Copy copy = new Copy();
        copy.setCopyId(mockCopyId);
        copy.setBorrower(mockUsername);
        copy.setStatus("不在库");
        ResponseEntity<Copy> responseEntity1 = ResponseEntity.ok(copy);


        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        Mockito.when(borrowRepository.existsByCopyId(mockCopyId)).thenReturn(true); //书本在外
        Mockito.when(restTemplate.getForEntity("http://localhost:9091/api/book/copy/" + mockCopyId, Copy.class))
                .thenReturn(responseEntity1);


        HashMap<String, Object> map = borrowService.borrow(request).getMap();
        assertEquals("只能借阅在库副本",map.get("message"));
        System.out.println(map);

    }

    @Test
    void  borrowWithoutRule() {
        BorrowRequest request = new BorrowRequest();
        request.setSession(mockSession);
        request.setCopyId(mockCopyId);
        request.setBorrower(mockUsername);

        User user = new User();
        user.setPermission("管理员");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);

        Copy copy = new Copy();
        copy.setCopyId(mockCopyId);
        copy.setBorrower(mockUsername);
        copy.setStatus("在库");
        ResponseEntity<Copy> responseEntity1 = ResponseEntity.ok(copy);


        ResponseEntity<String> responseEntity2 = ResponseEntity.ok(mockIdentity);

        Rule rule = null;

        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        Mockito.when(borrowRepository.existsByCopyId(mockCopyId)).thenReturn(false); //书本没被借出
        Mockito.when(restTemplate.getForEntity("http://localhost:9091/api/book/copy/" + mockCopyId, Copy.class))
                .thenReturn(responseEntity1);
        Mockito.when(restTemplate.getForEntity("http://localhost:9090/api/identity/" + mockUsername, String.class))
                .thenReturn(responseEntity2);
        Mockito.when(ruleRepository.findRuleByIdentity(mockIdentity)).thenReturn(rule);


        HashMap<String, Object> map = borrowService.borrow(request).getMap();
        assertEquals("读者用户名不存在或身份异常,请检查",map.get("message"));
        System.out.println(map);

    }

    @Test
    void  borrowOverLimit() {
        BorrowRequest request = new BorrowRequest();
        request.setSession(mockSession);
        request.setCopyId(mockCopyId);
        request.setBorrower(mockUsername);

        User user = new User();
        user.setPermission("管理员");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);

        Copy copy = new Copy();
        copy.setCopyId(mockCopyId);
        copy.setBorrower(mockUsername);
        copy.setStatus("在库");
        ResponseEntity<Copy> responseEntity1 = ResponseEntity.ok(copy);


        ResponseEntity<String> responseEntity2 = ResponseEntity.ok(mockIdentity);

        Rule rule = new Rule();
        rule.setBorrowMaxNum(3);


        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        Mockito.when(borrowRepository.existsByCopyId(mockCopyId)).thenReturn(false); //书本没被借出
        Mockito.when(restTemplate.getForEntity("http://localhost:9091/api/book/copy/" + mockCopyId, Copy.class))
                .thenReturn(responseEntity1);
        Mockito.when(restTemplate.getForEntity("http://localhost:9090/api/identity/" + mockUsername, String.class))
                .thenReturn(responseEntity2);
        Mockito.when(ruleRepository.findRuleByIdentity(mockIdentity)).thenReturn(rule);
        Mockito.when(borrowRepository.countByBorrower(request.getBorrower())).thenReturn(4);


        HashMap<String, Object> map = borrowService.borrow(request).getMap();
        assertEquals("预约或借书数量已达上限",map.get("message"));
        System.out.println(map);

    }

    @Test
    void  borrowWithWrongBrunch() {
        BorrowRequest request = new BorrowRequest();
        request.setSession(mockSession);
        request.setCopyId(mockCopyId);
        request.setBorrower(mockUsername);
        request.setBranch("鲸鱼岛");

        User user = new User();
        user.setPermission("管理员");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);

        Copy copy = new Copy();
        copy.setCopyId(mockCopyId);
        copy.setBorrower(mockUsername);
        copy.setStatus("在库");
        copy.setBranch("恐龙园");
        ResponseEntity<Copy> responseEntity1 = ResponseEntity.ok(copy);


        ResponseEntity<String> responseEntity2 = ResponseEntity.ok(mockIdentity);

        Rule rule = new Rule();
        rule.setBorrowMaxNum(5);


        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        Mockito.when(borrowRepository.existsByCopyId(mockCopyId)).thenReturn(false); //书本没被借出
        Mockito.when(restTemplate.getForEntity("http://localhost:9091/api/book/copy/" + mockCopyId, Copy.class))
                .thenReturn(responseEntity1);
        Mockito.when(restTemplate.getForEntity("http://localhost:9090/api/identity/" + mockUsername, String.class))
                .thenReturn(responseEntity2);
        Mockito.when(ruleRepository.findRuleByIdentity(mockIdentity)).thenReturn(rule);
        Mockito.when(borrowRepository.countByBorrower(request.getBorrower())).thenReturn(4);


        HashMap<String, Object> map = borrowService.borrow(request).getMap();
        assertEquals(copy.getCopyId() + ":不能借阅其他分馆的副本",map.get("message"));
        System.out.println(borrowService.borrow(request).getMap());

    }

    @Test
    void  borrowWithLowCredit() {
        BorrowRequest request = new BorrowRequest();
        request.setSession(mockSession);
        request.setCopyId(mockCopyId);
        request.setBorrower(mockUsername);
        request.setBranch("恐龙园");

        User user = new User();
        user.setPermission("管理员");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);

        Copy copy = new Copy();
        copy.setCopyId(mockCopyId);
        copy.setBorrower(mockUsername);
        copy.setStatus("在库");
        copy.setBranch("恐龙园");
        ResponseEntity<Copy> responseEntity1 = ResponseEntity.ok(copy);


        ResponseEntity<String> responseEntity2 = ResponseEntity.ok(mockIdentity);


        Rule rule = new Rule();
        rule.setBorrowMaxNum(5);

        ResponseEntity<Integer> responseEntity3 = ResponseEntity.ok(0);//信用分为0

        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        Mockito.when(borrowRepository.existsByCopyId(mockCopyId)).thenReturn(false); //书本没被借出
        Mockito.when(restTemplate.getForEntity("http://localhost:9091/api/book/copy/" + mockCopyId, Copy.class))
                .thenReturn(responseEntity1);
        Mockito.when(restTemplate.getForEntity("http://localhost:9090/api/identity/" + mockUsername, String.class))
                .thenReturn(responseEntity2);
        Mockito.when(ruleRepository.findRuleByIdentity(mockIdentity)).thenReturn(rule);
        Mockito.when(borrowRepository.countByBorrower(request.getBorrower())).thenReturn(4);
        Mockito.when(restTemplate.getForEntity("http://localhost:9090/api/credit/" + mockUsername, Integer.class))
                .thenReturn(responseEntity3);


        HashMap<String, Object> map = borrowService.borrow(request).getMap();
        assertEquals("用户信用为0,支付罚单或申请恢复后才能借书",map.get("message"));
        System.out.println(map);

    }

    @Test
    void  borrowSuccessfully() {
        BorrowRequest request = new BorrowRequest();
        request.setSession(mockSession);
        request.setCopyId(mockCopyId);
        request.setBorrower(mockUsername);
        request.setBranch("恐龙园");

        User user = new User();
        user.setPermission("管理员");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);

        Copy copy = new Copy();
        copy.setCopyId(mockCopyId);
        copy.setBorrower(mockUsername);
        copy.setTitle(mockTitle);
        copy.setStatus("在库");
        copy.setBranch("恐龙园");
        ResponseEntity<Copy> responseEntity1 = ResponseEntity.ok(copy);

        ResponseEntity<String> responseEntity2 = ResponseEntity.ok(mockIdentity);

        Rule rule = new Rule();
        rule.setBorrowMaxNum(5);
        rule.setBorrowValidTime(100);

        ResponseEntity<Integer> responseEntity3 = ResponseEntity.ok(100);//信用分为100

        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        Mockito.when(borrowRepository.existsByCopyId(mockCopyId)).thenReturn(false); //书本没被借出
        Mockito.when(restTemplate.getForEntity("http://localhost:9091/api/book/copy/" + mockCopyId, Copy.class))
                .thenReturn(responseEntity1);
        Mockito.when(restTemplate.getForEntity("http://localhost:9090/api/identity/" + mockUsername, String.class))
                .thenReturn(responseEntity2);
        Mockito.when(ruleRepository.findRuleByIdentity(mockIdentity)).thenReturn(rule);
        Mockito.when(borrowRepository.countByBorrower(request.getBorrower())).thenReturn(4);
        Mockito.when(restTemplate.getForEntity("http://localhost:9090/api/credit/" + mockUsername, Integer.class))
                .thenReturn(responseEntity3);


        HashMap<String, Object> map = borrowService.borrow(request).getMap();
        assertEquals("借书成功",map.get("message"));;
        System.out.println(map);

    }

    /**
     * return  错误性检测基本上与borrow一致，这里主要做成功的检测
     */
    @Test
    void returnSuccessfully(){
        ReturnRequest request = new ReturnRequest();
        request.setSession(mockSession);
        request.setCopyId(mockCopyId);
        request.setBranch("恐龙园");
        request.setStatus("完好");

        User user = new User();
        user.setPermission("管理员");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);


        Copy copy = new Copy();
        copy.setCopyId(mockCopyId);
        copy.setBorrower(mockUsername);
        copy.setTitle(mockTitle);
        copy.setISBN("");
        copy.setStatus("借出");
        copy.setBranch("恐龙园");
        ResponseEntity<Copy> responseEntity1 = ResponseEntity.ok(copy);

        Borrow borrow = new Borrow();
        borrow.setBorrower(mockUsername);
        borrow.setValidTime(new Date());


        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        Mockito.when(borrowRepository.existsByCopyIdAndStatus(mockCopyId, "借出"))
                .thenReturn(true);//借出状态
        Mockito.when(restTemplate.getForEntity("http://localhost:9091/api/book/copy/" + mockCopyId, Copy.class))
                .thenReturn(responseEntity1);
        Mockito.when(restTemplate.getForObject("http://localhost:9091/api/book/price/" + copy.getISBN(), Double.class))
                .thenReturn(40.00);
        Mockito.when(borrowRepository.findBorrowByCopyId(copy.getCopyId())).thenReturn(borrow);

        HashMap<String, Object> map = borrowService.retCopy(request).getMap();
        assertEquals("还书成功",map.get("message"));//测试过超期，罚款，也都没问题
        System.out.println(map);

    }

    /**
     * reserve  错误性检测基本上与borrow一致，这里主要做成功的检测
     */
    @Test
    void reserveSuccessfully(){
        ReserveRequest request = new ReserveRequest();
        request.setSession(mockSession);
        request.setCopyId(mockCopyId);


        User user = new User();
        user.setPermission("管理员");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);

        Copy copy = new Copy();
        copy.setCopyId(mockCopyId);
        copy.setBorrower(mockUsername);
        copy.setTitle(mockTitle);
        copy.setStatus("在库");
        copy.setBranch("恐龙园");
        ResponseEntity<Copy> responseEntity1 = ResponseEntity.ok(copy);

        ResponseEntity<String> responseEntity2 = ResponseEntity.ok(mockIdentity);
        ResponseEntity<Integer> responseEntity3 = ResponseEntity.ok(100);//信用分为100

        Rule rule = new Rule();
        rule.setBorrowMaxNum(5);
        rule.setReserveValidTime(100);

        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        Mockito.when(borrowRepository.existsByCopyId(mockCopyId)).thenReturn(false); //书本没被借出
        Mockito.when(restTemplate.getForEntity("http://localhost:9091/api/book/copy/" + mockCopyId, Copy.class))
                .thenReturn(responseEntity1);
        Mockito.when(restTemplate.getForEntity("http://localhost:9090/api/identity/" + mockUsername, String.class))
                .thenReturn(responseEntity2);
        Mockito.when(ruleRepository.findRuleByIdentity(mockIdentity)).thenReturn(rule);
        Mockito.when(borrowRepository.countByBorrower(user.getUsername())).thenReturn(4);
        Mockito.when(restTemplate.getForEntity("http://localhost:9090/api/credit/" + mockUsername, Integer.class))
                .thenReturn(responseEntity3);

        HashMap<String, Object> map = borrowService.reserve(request).getMap();
        assertEquals("预约成功",map.get("message"));
        System.out.println(map);

    }

    /**
     * getReserved 这里主要做成功的检测
     */
    @Test
    void getReservedSuccessfully(){
        BorrowRequest request = new BorrowRequest();
        request.setSession(mockSession);
        request.setCopyId(mockCopyId);
        request.setBorrower(mockUsername);
        request.setBranch("恐龙园");


        User user = new User();
        user.setPermission("管理员");
        user.setUsername(mockUsername);
        ResponseEntity<User> responseEntity = ResponseEntity.ok(user);

        Copy copy = new Copy();
        copy.setCopyId(mockCopyId);
        copy.setBorrower(mockUsername);
        copy.setTitle(mockTitle);
        copy.setStatus("预约");
        copy.setBranch("恐龙园");
        ResponseEntity<Copy> responseEntity1 = ResponseEntity.ok(copy);

        ResponseEntity<String> responseEntity2 = ResponseEntity.ok(mockIdentity);
        ResponseEntity<Integer> responseEntity3 = ResponseEntity.ok(100);//信用分为100

        Rule rule = new Rule();
        rule.setBorrowMaxNum(5);
        rule.setReserveValidTime(100);
        rule.setBorrowValidTime(200);

        Borrow borrow = new Borrow();
        borrow.setBorrower(mockUsername);
        borrow.setValidTime(new Date());

        Mockito.when(restTemplate.postForEntity("http://localhost:9090/api/session/" + mockSession, null, User.class))
                .thenReturn(responseEntity);
        Mockito.when(borrowRepository.existsByCopyIdAndStatus(mockCopyId, "预约")).thenReturn(true); //书本没被借出
        Mockito.when(restTemplate.getForEntity("http://localhost:9091/api/book/copy/" + mockCopyId, Copy.class))
                .thenReturn(responseEntity1);
        Mockito.when(restTemplate.getForEntity("http://localhost:9090/api/identity/" + mockUsername, String.class))
                .thenReturn(responseEntity2);
        Mockito.when(ruleRepository.findRuleByIdentity(mockIdentity)).thenReturn(rule);
        Mockito.when(borrowRepository.findBorrowByCopyId(copy.getCopyId())).thenReturn(borrow);
        Mockito.when(restTemplate.getForEntity("http://localhost:9090/api/credit/" + mockUsername, Integer.class))
                .thenReturn(responseEntity3);

        HashMap<String, Object> map = borrowService.getReserve(request).getMap();
        assertEquals("取预约成功",map.get("message"));
        System.out.println(map);

    }






}
