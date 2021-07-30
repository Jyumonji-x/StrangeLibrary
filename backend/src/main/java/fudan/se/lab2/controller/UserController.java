package fudan.se.lab2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fudan.se.lab2.controller.request.UserModifyRequest;
import fudan.se.lab2.service.UserService;
import fudan.se.lab2.controller.request.LoginRequest;
import fudan.se.lab2.controller.request.RegisterRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author WangHaiWei, ZhangMing
 */
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/user/captcha/{username}")
    public ResponseEntity<?> captcha(@PathVariable String username) throws JsonProcessingException {
        System.out.println("UserController /api/user/captcha:");
        System.out.println("username = " + username);
        return ResponseEntity.ok(userService.captcha(username).toJson());
    }

    @PostMapping("/api/user/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) throws JsonProcessingException {
        System.out.println("UserController /api/user/register called:");
        System.out.println(request);
        return ResponseEntity.ok(userService.register(request).toJson());
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) throws JsonProcessingException {
        System.out.println("UserController /api/user/login called:");
        System.out.println("Param user_name :" + request.getUser_name());
        System.out.println("Param password :" + request.getPassword());
        return ResponseEntity.ok(userService.login(request).toJson());
    }

    @PostMapping("/api/user/modify")
    public ResponseEntity<?> modify(@RequestBody UserModifyRequest request) throws JsonProcessingException {
        System.out.println("UserController /api/user/modify called:");
        System.out.println(request);
        return ResponseEntity.ok((userService.modify(request)));
    }

    /**
     * 健康测试
     */
    @GetMapping("/welcome")
    public ResponseEntity<?> welcome() throws JSONException {
        JSONObject jsonMap = new JSONObject();
        jsonMap.put("rtn", 1);
        jsonMap.put("message", "成功");
        return ResponseEntity.ok(jsonMap.toString());
    }
}







