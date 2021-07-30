package se24.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se24.userservice.controller.request.CreateAdminRequest;
import se24.userservice.controller.request.LoginRequest;
import se24.userservice.controller.request.ModifyRequest;
import se24.userservice.controller.request.RegisterRequest;
import se24.userservice.domain.User;
import se24.userservice.repository.UserRepository;
import se24.userservice.service.UserService;
import se24.userservice.tool.ReturnMap;
import se24.userservice.tool.SessionMap;

import javax.validation.Valid;
import javax.validation.constraints.Max;

@RestController
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @GetMapping("/api/identity/{username}")
    public ResponseEntity<String> getIdentity(
            @PathVariable String username
    ) {
        System.out.println("服务调用接口，使用username获取identity");
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.ok(user.getIdentity());
        }
    }

    @PutMapping("/api/credit/{username}/{change}")
    public ResponseEntity<Integer> changeCredit(
            @PathVariable String username,
            @PathVariable int change
    ) {
        System.out.println("服务调用接口，根据username修改credit");
        User user = userRepository.findUserByUsername(username);
        System.out.println(user);
        if (user == null) {
            return ResponseEntity.ok(-1);
        } else {
            int credit = user.getCredit();
            int creditChanged = credit + change;
            if (creditChanged > 100) {
                creditChanged = 100;
            }
            if (creditChanged < 0) {
                creditChanged = 0;
            }
            user.setCredit(creditChanged);
            userRepository.save(user);
            return ResponseEntity.ok(user.getCredit());
        }
    }

    @GetMapping("/api/credit/{username}")
    public ResponseEntity<Integer> getCredit(
            @PathVariable String username
    ) {
        System.out.println("服务调用接口，使用username获取credit");
        User user = userRepository.findUserByUsername(username);
        System.out.println(user);
        if (user == null) {
            return ResponseEntity.ok(0);
        } else {
            return ResponseEntity.ok(user.getCredit());
        }
    }

    @PostMapping("/api/session/{session}")
    public ResponseEntity<?> getUser(
            @PathVariable String session
    ) {
        System.out.println("服务调用接口，使用session获取用户");
        User user = SessionMap.getUser(session);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/authority/create_admin")
    public ResponseEntity<?> createAdmin(
            @RequestBody @Valid CreateAdminRequest request,
            BindingResult result
    ) {
        System.out.println("创建管理员接口,参数检验阶段");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 注册
            map = userService.createAdmin(request);
        }
        return ResponseEntity.ok(map.getMap());
    }

    // 获取一个验证码,发送到对应学邮
    @GetMapping("/api/user/captcha/{username}")
    public ResponseEntity<?> captcha(@PathVariable String username) {
        System.out.println("注册验证码发送接口");
        System.out.println("username = " + username);
        return ResponseEntity.ok(userService.captcha(username).getMap());
    }

    // 注册,接受用户名,密码,验证码
    @PostMapping("/api/user/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request,
                                      BindingResult result) {
        System.out.println("注册接口,参数检验阶段");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 注册
            map = userService.register(request);
        }
        return ResponseEntity.ok(map.getMap());
    }

    // 登录,接受用户名,密码
    @PostMapping("/api/user/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request,
                                   BindingResult result) {
        System.out.println("登录接口，参数检验阶段");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 注册
            map = userService.login(request);
        }
        return ResponseEntity.ok(map.getMap());
    }

    // 接受session,新密码
    @PostMapping("/api/user/modify")
    public ResponseEntity<?> modify(@RequestBody @Valid ModifyRequest request,
                                    BindingResult result) {
        System.out.println("UserController /api/user/modify called:");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 注册
            map = userService.modify(request);
        }
        return ResponseEntity.ok(map.getMap());
    }
}
