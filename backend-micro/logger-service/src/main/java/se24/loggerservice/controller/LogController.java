package se24.loggerservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se24.loggerservice.domain.Log;
import se24.loggerservice.service.LogService;

@RestController
public class LogController {
    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PutMapping("/api/logger/log")
    public ResponseEntity<?> log(
            @RequestBody Log log
    ) {
        System.out.println("系统记录服务，添加记录方法");
        System.out.println(log);
        return ResponseEntity.ok(logService.add(log).getMap());
    }

    @GetMapping("/api/logger/username/{username}")
    public ResponseEntity<?> getByName(@PathVariable String username) {
        System.out.println("根据用户名查询");
        return ResponseEntity.ok(logService.getByUsername(username).getMap());
    }

    @GetMapping("/api/logger/copyId/{copyId}")
    public ResponseEntity<?> getByCopyId(@PathVariable String copyId) {
        System.out.println("根据副本Id查询");
        return ResponseEntity.ok(logService.getByCopyId(copyId).getMap());
    }


    //许同樵 前端debug 添加
    @GetMapping("/api/logger/getAll/")
    public ResponseEntity<?> getAll() {
        System.out.println("查找所有的");
        return ResponseEntity.ok(logService.getAll().getMap());
    }
}
