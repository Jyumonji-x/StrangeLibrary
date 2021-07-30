package se24.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se24.userservice.controller.request.ApplyRequest;
import se24.userservice.controller.request.ApproveRequest;
import se24.userservice.domain.Application;
import se24.userservice.repository.ApplicationRepository;
import se24.userservice.repository.UserRepository;
import se24.userservice.service.ApplicationService;
import se24.userservice.tool.ReturnMap;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ApplicationController {
    private final ApplicationService applicationService;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    @Autowired
    public ApplicationController(ApplicationService applicationService, ApplicationRepository applicationRepository, UserRepository userRepository) {
        this.applicationService = applicationService;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/api/application")
    public ResponseEntity<?> get(){
        List<Application> applications = applicationRepository.findAll();
        return ResponseEntity.ok(applications);
    }

    // 申请恢复分数接口 接受用户session、理由
    @PostMapping("/api/application/apply")
    public ResponseEntity<?> apply(
            @RequestBody @Valid ApplyRequest request,
            BindingResult result) {
        System.out.println("申请恢复分数接口,参数检验阶段");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 申请
            map = applicationService.apply(request);
        }
        return ResponseEntity.ok(map.getMap());
    }


    // 批准接口 接受管理员session、申请id
    @PostMapping("/api/application/approve")
    public ResponseEntity<?> approve(
            @RequestBody @Valid ApproveRequest request,
            BindingResult result
    ) {
        System.out.println("批准恢复分数申请接口,参数检验阶段");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 批准
            map = applicationService.approve(request);
        }
        return ResponseEntity.ok(map.getMap());
    }
}
