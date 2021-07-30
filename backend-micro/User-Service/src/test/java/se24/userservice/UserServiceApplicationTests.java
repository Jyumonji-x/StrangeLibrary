package se24.userservice;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import se24.userservice.controller.request.CreateAdminRequest;
import se24.userservice.controller.request.LoginRequest;
import se24.userservice.controller.request.ModifyRequest;
import se24.userservice.controller.request.RegisterRequest;
import se24.userservice.domain.User;
import se24.userservice.repository.UserRepository;
import se24.userservice.service.UserService;
import se24.userservice.tool.CaptchaMap;
import se24.userservice.tool.ReturnMap;
import se24.userservice.tool.SessionMap;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class UserServiceApplicationTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private UserService userService;

    private final String mockUsername = "19302010020";
    private final String mockPassword = "abcd1234";

    /**
     * createAdmin
     **/
    @Test
    void createAdmin() {
        User admin = new User();
        admin.setPermission("超级管理员");
        admin.setUsername("lbwnb");
        String session = SessionMap.login(admin);
        CreateAdminRequest request = new CreateAdminRequest();
        request.setSession(session);
        request.setUsername(mockUsername);
        request.setPassword(mockPassword);
        HashMap<String, Object> map = userService.createAdmin(request).getMap();
        assertEquals("创建管理员成功", map.get("message"));
    }

    @Test
    void createAdminWithoutPermission() {
        User admin = new User();
        admin.setPermission("管理员");
        admin.setUsername("lbwnb");
        String session = SessionMap.login(admin);
        CreateAdminRequest request = new CreateAdminRequest();
        request.setSession(session);
        request.setUsername(mockUsername);
        request.setPassword(mockPassword);
        HashMap<String, Object> map = userService.createAdmin(request).getMap();
        assertEquals("创建管理员需要超级管理员权限", map.get("message"));
        System.out.println(map);
    }

    /**
     * captcha
     **/

    @Test
    void captchaForExistUser() {
        Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(true);
        HashMap<String, Object> map = userService.captcha("19302010020").getMap();
        assertEquals("用户名已存在", map.get("message"));
    }

    @Test
    void captchaForNewUser() {
        ReturnMap rtnMap = new ReturnMap();
        rtnMap.setRtn(1);
        rtnMap.setMessage("验证码邮件发送成功");
        Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        ResponseEntity responseEntity = ResponseEntity.ok(rtnMap.getMap());
        Mockito.when(restTemplate.postForEntity(Mockito.anyString(), Mockito.any(), Mockito.any()))
                .thenReturn(responseEntity);
        HashMap<String, Object> map = userService.captcha("19302010020").getMap();
        System.out.println(map);
        assertEquals("验证码邮件发送成功", map.get("message"));
    }

    @Test
    void captchaForUsernameTooLong() {
        Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        HashMap<String, Object> map = userService.captcha("193020100200").getMap();
        assertEquals("请使用有效的复旦大学学工号进行注册", map.get("message"));
    }

    @Test
    void captchaForUsernameInvalid() {
        Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(false);
        HashMap<String, Object> map = userService.captcha("19702010020").getMap();
        assertEquals("请使用有效的复旦大学学工号进行注册", map.get("message"));
    }

    /**
     * modify
     **/

    @Test
    void modifyBeforeLogin() {
        User mockUser = new User();
        mockUser.setUsername(mockUsername);
        mockUser.setPassword(mockPassword);
        ModifyRequest request = new ModifyRequest();
        request.setSession("123456");
        request.setPassword("abcd1111");
        HashMap<String, Object> map = userService.modify(request).getMap();
        assertEquals("只能修改当前登录用户的密码", map.get("message"));
    }

    @Test
    void modify() {
        User mockUser = new User();
        mockUser.setUsername(mockUsername);
        mockUser.setPassword(mockPassword);
        String session = SessionMap.login(mockUser);
        ModifyRequest request = new ModifyRequest();
        request.setSession(session);
        request.setPassword("abcd1111");
        HashMap<String, Object> map = userService.modify(request).getMap();
        assertEquals("修改密码成功", map.get("message"));
    }

    /**
     * login
     **/

    @Test
    void loginNonExistentUser() {
        LoginRequest request = new LoginRequest();
        request.setPassword(mockPassword);
        request.setPassword(mockPassword);
        HashMap<String, Object> map = userService.login(request).getMap();
        assertEquals("用户名或密码错误", map.get("message"));
    }

    @Test
    void loginCorrectly() {
        User mockUser = new User();
        mockUser.setUsername(mockUsername);
        mockUser.setPassword(mockPassword);
        Mockito.when(userRepository.findUserByUsername(mockUsername)).thenReturn(mockUser);

        LoginRequest request = new LoginRequest();
        request.setUsername(mockUsername);
        request.setPassword(mockPassword);
        HashMap<String, Object> map = userService.login(request).getMap();
        assertEquals("登录成功", map.get("message"));
        System.out.println(map);
    }

    @Test
    void loginWithWrongPassword() {
        User mockUser = new User();
        mockUser.setUsername(mockUsername);
        mockUser.setPassword(mockPassword);
        Mockito.when(userRepository.findUserByUsername(mockUsername)).thenReturn(mockUser);

        LoginRequest request = new LoginRequest();
        request.setUsername(mockUsername);
        request.setPassword("aaaa1111");
        HashMap<String, Object> map = userService.login(request).getMap();
        assertEquals("用户名或密码错误", map.get("message"));
        System.out.println(map);
    }

    /**
     * register
     **/

    @Test
    void registerWithExistUser() {
        Mockito.when(userRepository.existsByUsername(Mockito.anyString())).thenReturn(true);
        RegisterRequest request = new RegisterRequest();
        request.setUsername(mockUsername);
        HashMap<String, Object> map = userService.register(request).getMap();
        assertEquals("用户名已存在", map.get("message"));
    }

    @Test
    void registerWithWrongCaptcha() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(mockUsername);
        request.setCaptcha(114514);
        HashMap<String, Object> map = userService.register(request).getMap();
        assertEquals("验证码错误", map.get("message"));
    }

    @Test
    void registerCorrectly() {
        User mockUser = new User();
        mockUser.setUsername(mockUsername);
        mockUser.setPassword(mockPassword);
        int captcha = CaptchaMap.generateCaptcha(mockUsername);

        RegisterRequest request = new RegisterRequest();
        request.setUsername(mockUsername);
        request.setPassword(mockPassword);
        request.setCaptcha(captcha);

        HashMap<String, Object> map = userService.register(request).getMap();
        assertEquals("注册成功", map.get("message"));
    }
}
