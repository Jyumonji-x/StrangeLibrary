package fudan.se.lab2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import fudan.se.lab2.controller.request.LoginRequest;
import fudan.se.lab2.controller.request.RegisterRequest;
import fudan.se.lab2.controller.request.UserModifyRequest;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.store.CaptchaMap;
import fudan.se.lab2.util.JsonMap;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private final String username = "19302010000";
    private final String password = "abcd1234";
    private final RegisterRequest registerExample = new RegisterRequest(username, "19102010000@fudan.edu.cn", "abcd1234", 1111);
    private final LoginRequest loginExample = new LoginRequest(username, password);

    @Test
    void sendCaptchaEmailTest() {
        JsonMap result = userService.captcha("19302010020");
        assertEquals("验证码发送成功", result.get("message"));
    }

    @Test
    void registerWithWrongCaptcha() {
        JsonMap result = userService.register(registerExample);
        assertEquals(0, result.get("rtn"));
        assertEquals("验证码错误", result.get("message"));
    }

    @Test
    void registerWithRightCaptcha() {
        int captcha = CaptchaMap.generateCaptcha(username);
        registerExample.setCaptcha(captcha);
        userService.register(registerExample);
        JsonMap result = userService.register(registerExample);
        assertEquals(0, result.get("rtn"));
        assertEquals("用户名已被注册，请更换\n", result.get("message"));
    }

    @Test
    void loginSuccess() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);
        JsonMap result = userService.login(loginExample);
        System.out.println(result);
        assertEquals(1, result.get("rtn"));
        assertEquals("登录成功", result.get("message"));
    }

    @Test
    void loginFailed() {
        JsonMap result = userService.login(loginExample);
        assertEquals(0, result.get("rtn"));
        assertEquals("用户名或者密码不正确", result.get("message"));
    }


    @Test
    void modify() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);

        UserModifyRequest request = new UserModifyRequest();
        request.setUser_name(username);
        request.setPassword(password + "1");
        request.setOld_password(password);
        JsonMap modify = userService.modify(request);
        assertEquals("密码修改成功", modify.get("message"));
        assertEquals(0, userService.login(loginExample).get("rtn"));
    }

    @Test
    void modifyNullAccount() {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);

        UserModifyRequest request = new UserModifyRequest();
        request.setUser_name(username + "1");
        request.setPassword(password + "1");
        request.setOld_password(password);
        JsonMap modify = userService.modify(request);
        assertEquals("找不到用户", modify.get("message"));
        assertEquals(1, userService.login(loginExample).get("rtn"));
    }
}
