package se24.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se24.userservice.controller.request.ApplyRequest;
import se24.userservice.controller.request.ApproveRequest;
import se24.userservice.domain.Application;
import se24.userservice.domain.Log;
import se24.userservice.domain.User;
import se24.userservice.repository.ApplicationRepository;
import se24.userservice.repository.UserRepository;
import se24.userservice.tool.ReturnMap;
import se24.userservice.tool.SessionMap;

import java.util.Date;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, UserRepository userRepository, RestTemplate restTemplate) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    public ReturnMap apply(ApplyRequest request) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        User user = SessionMap.getUser(request.getSession());
        if (user == null) {
            map.setMessage("需要登录才能申请恢复信用");
            return map;
        }
        if (applicationRepository.existsApplicationByUsername(user.getUsername())) {
            map.setMessage("不能重复申请");
            return map;
        }
        return doApply(request, user);
    }

    private ReturnMap doApply(ApplyRequest request, User user) {
        Date now = new Date();
        Application application = new Application();
        application.setUsername(user.getUsername());
        application.setTime(now);
        application.setReason(request.getReason());
        applicationRepository.save(application);

        // 系统记录
        Log log = new Log();
        log.setUsername(user.getUsername());
        log.setCategory("申请恢复信用");
        log.setPrice(0);
        log.setTime(now);
        log.setNote("申请理由:" + request.getReason());
        restTemplate.put("http://localhost:9099/api/logger/log", log);

        ReturnMap map = new ReturnMap();
        map.setRtn(1);
        map.setMessage("申请成功,请等待管理员审批");
        return map;
    }

    // 检验admin权限 检验申请
    public ReturnMap approve(ApproveRequest request) {
        ReturnMap map = new ReturnMap();
        map.setRtn(0);
        User admin = SessionMap.getUser(request.getSession());
        if (admin == null) {
            map.setMessage("需要管理员权限才能批准");
            return map;
        }
        String username = request.getUsername();
        if (applicationRepository.existsApplicationByUsername(username)) {
            // 恢复信用
            User user = userRepository.findUserByUsername(username);
            user.setCredit(100);
            userRepository.save(user);
            // 留下记录
            Date now = new Date();
            Log log = new Log();
            log.setUsername(user.getUsername());
            log.setCategory("批准信用恢复申请");
            log.setOperator(admin.getUsername());
            log.setPrice(0);
            log.setTime(now);
            restTemplate.put("http://localhost:9099/api/logger/log", log);
            // 删除
            applicationRepository.deleteApplicationByUsername(username);
            map.setRtn(1);
            map.setMessage("已批准"+username+"的申请");
            return map;
        } else {
            map.setMessage("该用户没有对应申请");
            return map;
        }
    }
}
