package se24.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se24.userservice.controller.request.*;
import se24.userservice.domain.User;
import se24.userservice.repository.UserRepository;
import se24.userservice.tool.CaptchaMap;
import se24.userservice.tool.ReturnMap;
import se24.userservice.tool.SessionMap;

import java.util.Date;
import java.util.HashMap;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public UserService(UserRepository userRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * createAdmin
     **/

    public ReturnMap createAdmin(CreateAdminRequest request) {
        String session = request.getSession();
        User user = SessionMap.getUser(session);
        ReturnMap map = new ReturnMap();
        if (user == null || !user.getPermission().equals("超级管理员")) {
            // 检测超管权限
            map.setRtn(0);
            map.setMessage("创建管理员需要超级管理员权限");
        } else if (userRepository.existsByUsername(request.getUsername())) {
            // 检测用户名是否已存在
            map.setRtn(0);
            map.setMessage("用户名已存在");
        } else {
            return doCreateAdmin(request);
        }
        return map;
    }

    private ReturnMap doCreateAdmin(CreateAdminRequest request) {
        String password = request.getPassword();
        String username = request.getUsername();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        // 其实email也不一定需要，但姑且留着
        user.setEmail(username + "@fudan.edu.cn");
        user.setPermission("管理员");
        user.setTime_create(new Date());
        userRepository.save(user);

        ReturnMap map = new ReturnMap();
        map.setMessage("创建管理员成功");
        map.setRtn(1);
        return map;
    }

    /**
     * captcha
     **/
    public ReturnMap captcha(String username) {
        // 检测用户名存在性
        ReturnMap map = new ReturnMap();
        if (userRepository.existsByUsername(username)) {
            map.setRtn(0);
            map.setMessage("用户名已存在");
            return map;
        }
        // 检测用户名合法性
        try {
            getIdentity(username);
        } catch (WrongUserNameException e) {
//            e.printStackTrace();
            map.setRtn(0);
            map.setMessage("请使用有效的复旦大学学工号进行注册");
            return map;
        }
        return doCaptcha(username);
    }

    private ReturnMap doCaptcha(String username) {
        // 生成验证码并存储
        int captcha = CaptchaMap.generateCaptcha(username);

        SendMailRequest request = new SendMailRequest();
        request.setTarget(username + "@fudan.edu.cn");
        request.setTitle("StrangeLibrary:您正在尝试注册，复制验证码到注册页面");
        String text = "学工号" + username + "的用户您好，StrangeLibrary接收到您的注册请求。\n" +
                "请复制4位数字到注册界面的验证码输入框完成学邮验证：" + captcha + "\n" +
                "\n" +
                "提示：如果这不是您申请发送的验证码，请无视本邮件，且不要泄露验证码。";
        request.setContent(text);
        // 调用mail服务发送邮件
        ResponseEntity<HashMap> responseEntity = restTemplate.postForEntity("http://localhost:9080/api/mail/send", request, HashMap.class);
        // 这里的hashmap来自上面调用的接口，便于转发失败信息
        HashMap<String, Object> repMap = responseEntity.getBody();
        ReturnMap map = new ReturnMap();
        map.setMap(repMap);
        map.put("captcha", captcha);
        return map;
    }

    /**
     * modify
     **/

    public ReturnMap modify(ModifyRequest request) {
        // 检查session是否为已登录用户
        String session = request.getSession();
        User user = SessionMap.getUser(session);
        if (user == null) {
            ReturnMap map = new ReturnMap();
            map.setRtn(0);
            map.setMessage("只能修改当前登录用户的密码");
            return map;
        } else {
            return doModify(user, request.getPassword());
        }
    }

    private ReturnMap doModify(User user, String password) {
        ReturnMap map = new ReturnMap();
        map.setMessage("修改密码成功");
        map.setRtn(1);
        user.setPassword(password);
        userRepository.save(user);
        return map;
    }

    /**
     * login
     **/

    // 检测是否可以进行登录
    public ReturnMap login(LoginRequest request) {
        ReturnMap map = new ReturnMap();
        // 用户名和密码是否一致
        User user = userRepository.findUserByUsername(request.getUsername());
        if (user != null && user.getPassword().equals(request.getPassword())) {
            user.setTime_login(new Date());
            userRepository.save(user);
            return doLogin(user);
        } else {
            map.setRtn(0);
            map.setMessage("用户名或密码错误");
            return map;
        }
    }

    // 执行登录
    private ReturnMap doLogin(User user) {
        ReturnMap map = new ReturnMap();
        map.setRtn(1);
        map.setMessage("登录成功");
        String session = SessionMap.login(user);
        user.setPassword("no permission");
        map.put("user", user);
        map.put("session", session);
        return map;
    }

    /**
     * register
     **/

    // 检测是否可以进行用户注册
    public ReturnMap register(RegisterRequest request) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        String username = request.getUsername();
        Integer captcha = request.getCaptcha();
        if (userRepository.existsByUsername(username)) {
            // 用户名已存在
            map.setMessage("用户名已存在");
        } else if (!CaptchaMap.isValid(username, captcha)) {
            // 验证码错误
            map.setMessage("验证码错误");
        } else {
            // 通过验证，进行注册
            return doRegister(request);
        }
        return map;
    }

    // 检测通过，执行注册
    private ReturnMap doRegister(RegisterRequest request) {
        ReturnMap map = new ReturnMap();

        String username = request.getUsername();
        String password = request.getPassword();

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setTime_login(new Date());
        user.setEmail(username + "@fudan.edu.cn");
        user.setPermission("普通用户");
        try {
            user.setIdentity(getIdentity(username));
        } catch (WrongUserNameException e) {
//            e.printStackTrace();
            map.setRtn(0);
            map.setMessage("请使用有效的复旦大学学工号进行注册");
            return map;
        }
        user.setCredit(100);
        user.setTime_create(new Date());
        userRepository.save(user);
        map.setMessage("注册成功");
        user.setPassword("no permission");

        String session = SessionMap.login(user);
        map.put("session", session);
        map.put("user",user);
        map.setRtn(1);
        return map;
    }

    private String getIdentity(String username) throws WrongUserNameException {
        if (username.length() == 6) {
            return "教师";
        } else if (username.length() == 11) {
            switch (username.charAt(2)) {
                case '1':
                case '2':
                    return "研究生";
                case '3':
                    return "本科生";
                default:
                    throw new WrongUserNameException();
            }
        } else {
            throw new WrongUserNameException();
        }
    }

    private static class WrongUserNameException extends Exception {
        public WrongUserNameException() {
            super("用户名不合法");
        }
    }
}
