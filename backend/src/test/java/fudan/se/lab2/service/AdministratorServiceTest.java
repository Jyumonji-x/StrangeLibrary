package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.CreateAdminRequest;
import fudan.se.lab2.controller.request.LoginRequest;
import fudan.se.lab2.util.JsonMap;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class AdministratorServiceTest {
    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private UserService userService;

    @Test
    void superAdminExist() {
        assertNotNull(administratorService.superAdminExist());
    }

    @Test
    void creatNewAdministrator() {
        LoginRequest loginRequest = new LoginRequest("admin", "admin");
        JsonMap loginResult = userService.login(loginRequest);
        int adminToken = (int) loginResult.get("token");
        //创建一个管理员账户
        String user_name = "18300200009";
        String password = "abcd2333";
        String email = "18300200009@fudan.edu.cn";
        CreateAdminRequest request = new CreateAdminRequest(adminToken, user_name, password, email);
        JsonMap result = administratorService.creatAdministrator(request);
        assertEquals(1, result.get("rtn"));
        assertEquals("创建管理员成功", result.get("message"));
    }

    @Test
    void creatAdministratorWithoutPermission() {
        String user_name = "18300200009";
        String password = "abcd2333";
        String email = "18300200009@fudan.edu.cn";
        CreateAdminRequest request = new CreateAdminRequest(1, user_name, password, email);
        JsonMap result = administratorService.creatAdministrator(request);
        System.out.println(result);
        assertEquals(0, result.get("rtn"));
        assertEquals("只有超级管理员可以创建管理员", result.get("message"));
    }

    @Test
    void creatAdministratorWithDeficientParam() {
        LoginRequest loginRequest = new LoginRequest("admin", "admin");
        JsonMap loginResult = userService.login(loginRequest);
        int adminToken = (int) loginResult.get("token");
        String user_name = "18300200009";
        String password = "abcd2333";
        String email = "18300200009@fudan.edu.cn";
        CreateAdminRequest request = new CreateAdminRequest(adminToken, user_name, password, email);
        request.setUsername(null);
        JsonMap result = administratorService.creatAdministrator(request);
        System.out.println(result);
        assertEquals(0, result.get("rtn"));
        assertEquals("用户名不能为空", result.get("message"));
    }
}
