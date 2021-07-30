package fudan.se.lab2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fudan.se.lab2.controller.request.LoginRequest;
import fudan.se.lab2.controller.request.UserModifyRequest;

import fudan.se.lab2.enums.UserPermissionEnum;
import fudan.se.lab2.store.CaptchaMap;
import fudan.se.lab2.store.MyToken;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.repository.UserRepository;
import fudan.se.lab2.controller.request.RegisterRequest;
import fudan.se.lab2.util.AccountChecker;
import fudan.se.lab2.util.JsonMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

/**
 * @author ZhangMing, WangHaiWei
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final AccountChecker accountChecker;
    private final JavaMailSender javaMailSender;

    @Autowired
    public UserService(UserRepository userRepository, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.accountChecker = new AccountChecker(userRepository);
        this.javaMailSender = javaMailSender;
    }

    public JsonMap captcha(String username) {
        JsonMap map = new JsonMap();
        int rtn = 1;
        String message = "验证码发送成功";

        // 生成验证码并存储
        int captcha = CaptchaMap.generateCaptcha(username);

        // 向邮箱发送验证码
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("1534401580@qq.com");
        mail.setTo(username + "@fudan.edu.cn");
        mail.setSubject("StrangeLibrary:您正在尝试注册，复制验证码到注册页面");
        String text = "学工号" + username + "的用户您好，StrangeLibrary接收到您的注册请求。\n" +
                "请复制4位数字到注册界面的验证码输入框完成学邮验证：" + captcha + "\n" +
                "\n" +
                "提示：如果这不是您申请发送的验证码，请无视本邮件，且不要泄露验证码。";
        mail.setText(text);
        try {
            System.out.println("尝试发送邮件到：" + username + "@fudan.edu.cn");
            javaMailSender.send(mail);
        } catch (Exception e) {
            System.out.println("邮件发送出现问题：");
            System.out.println("接受者为：" + username);
            System.out.println("邮件内容为：" + text);
            System.out.println(e.getMessage());
            message = "验证码发送失败";
        }
        map.put("rtn", rtn);
        map.put("message", message);
        return map;
    }

    public JsonMap register(RegisterRequest request) {
        System.out.println("AuthService.register called");
        System.out.println(request);
        JsonMap map = new JsonMap();

        String username = request.getUser_name();
        String password = request.getPassword();
        String email = request.getEmail();
        int captcha = request.getCaptcha();

        int rtn = 1;
        String message = "";
        message += accountChecker.checkValidUsername(username);
        message += accountChecker.checkValidPassword(password);
        message += accountChecker.checkValidEmail(email);
        if (!CaptchaMap.isValid(username, captcha)) {
            rtn = 0;
            message = "验证码错误";
        }
        //验证通过，创建新用户
        if (message.equals("")) {
            String identity = "";
            //identity赋值
            if (username.length() == 6) {
                identity = "教师";
            } else {
                switch (username.charAt(2)) {
                    case '1':
                    case '2':
                        identity = "研究生";
                        break;
                    case '3':
                        identity = "本科生";
                        break;
                    default:
                        message = "请输入正确的学工号";
                        rtn = 0;
                }
            }
            if (!identity.isEmpty()) {
                User newUser = new User(username, password, email,
                        UserPermissionEnum.normalUser.getPermission(), new Date(), new Date(), identity);
                userRepository.save(newUser);

                message = "注册成功";
                Integer tokenId = MyToken.insertToken(newUser);
                System.out.println("token = " + tokenId);
                map.put("token", tokenId);
                // 除去密码返回给前端
                User rtnUser = newUser.clone();
                rtnUser.setPassword("");
                map.put("userDetails", rtnUser);
            }
        } else {
            rtn = 0;
        }
        map.put("rtn", rtn);
        map.put("message", message);
        return map;
    }

    public JsonMap login(LoginRequest request) {
        System.out.println("AuthService.login called");

        String username = request.getUser_name();
        String password = request.getPassword();

        JsonMap resultMap = new JsonMap();
        User user = userRepository.findByUsername(username);
        int rtn = 0;
        String message = "";
        if (user == null || !user.getPassword().equals(password)) {
            message = "用户名或者密码不正确";
        } else if (!user.isEnabled()) {
            message = "账号被禁用，请联系管理员";
        } else {
            user.setTime_login(new Date());
            Integer tokenId = MyToken.insertToken(user);
            userRepository.save(user);
            System.out.println("tokenId = " + tokenId);
            rtn = 1;
            message = "登录成功";
            // 除去密码发给前端
            User rtnUser = user.clone();
            rtnUser.setPassword("");
            resultMap.put("token", tokenId);
            resultMap.put("userDetails", rtnUser);
        }
        resultMap.put("rtn", rtn);
        resultMap.put("message", message);
        return resultMap;
    }

    public JsonMap modify(UserModifyRequest request) {
        System.out.println("AuthService.modify called");
        JsonMap resultMap = new JsonMap();

        int rtn = 0;
        String message = "";

        if (userRepository.findByUsername(request.getUser_name()) != null) {
            User user = userRepository.findByUsername(request.getUser_name());
            //验证密码是否正确
            if (!user.getPassword().equals(request.getOld_password())) {
                message = "密码不正确";
            } else {
                user.setPassword(request.getPassword());
                userRepository.save(user);
                rtn = 1;
                message = "密码修改成功";
            }
        } else {
            //找不到用户编号对应的用户，后期可以用security优化
            message = "找不到用户";
        }
        resultMap.put("rtn", rtn);
        resultMap.put("message", message);
        return resultMap;
    }
}

