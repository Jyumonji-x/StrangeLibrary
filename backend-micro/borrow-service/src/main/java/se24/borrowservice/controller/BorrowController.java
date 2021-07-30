package se24.borrowservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import se24.borrowservice.controller.request.BorrowRequest;
import se24.borrowservice.controller.request.ReserveRequest;
import se24.borrowservice.controller.request.ReturnRequest;
import se24.borrowservice.domain.Borrow;
import se24.borrowservice.repository.BorrowRepository;
import se24.borrowservice.service.BorrowService;
import se24.borrowservice.tool.ReturnMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class BorrowController {
    private final BorrowService borrowService;
    private final BorrowRepository borrowRepository;

    @Autowired
    public BorrowController(BorrowService borrowService, BorrowRepository borrowRepository) {
        this.borrowService = borrowService;
        this.borrowRepository = borrowRepository;
    }

    // 获取所有超时的借阅
    @GetMapping("/api/borrow/getOverdue")
    public ResponseEntity<List<Borrow>> getOverdue() {
        List<Borrow> borrows = borrowRepository.findBorrowsByValidTimeBeforeAndStatus(new Date(), "借出");
        if (borrows == null) {
            borrows = new ArrayList<>();
        }
        return ResponseEntity.ok(borrows);
    }

    @GetMapping("/api/borrow/get/{username}")
    public ResponseEntity<?> get(
            @PathVariable String username
    ){
        List<Borrow> borrows = borrowRepository.findBorrowsByBorrower(username);
        if (borrows==null){
            borrows=new ArrayList<>();
        }
        ReturnMap map=new ReturnMap();
        map.setRtn(1);
        map.setMessage("查询当前借书成功");
        map.put("borrows",borrows);
        return ResponseEntity.ok(map.getMap());
    }

    @PostMapping("/api/borrow/onsiteBorrow")
    public ResponseEntity<?> borrow(@RequestBody BorrowRequest request,
                                    BindingResult result) {
        System.out.println("现场借书接口");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 借书！
            map = borrowService.borrow(request);
        }
        return ResponseEntity.ok(map.getMap());
    }

    @PostMapping("/api/borrow/onsiteReturn")
    public ResponseEntity<?> returnCopy(@RequestBody ReturnRequest request,
                                        BindingResult result) {
        System.out.println("现场还书接口");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 还书！
            map = borrowService.retCopy(request);
        }
        return ResponseEntity.ok(map.getMap());
    }

    @PostMapping("/api/borrow/reserve")
    public ResponseEntity<?> reserve(@RequestBody ReserveRequest request,
                                     BindingResult result) {
        System.out.println("预约副本接口");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 预约！
            map = borrowService.reserve(request);
        }
        return ResponseEntity.ok(map.getMap());
    }


    @PostMapping("/api/borrow/getReserve")
    public ResponseEntity<?> reserve(@RequestBody BorrowRequest request,
                                     BindingResult result) {
        System.out.println("取预约副本接口");
        System.out.println(request);
        ReturnMap map = new ReturnMap();
        if (result.hasFieldErrors()) {
            map.setRtn(0);
            map.setMessage(result.getFieldError().getDefaultMessage());
        } else {
            // 参数检验通过 取书！
            map = borrowService.getReserve(request);
        }
        return ResponseEntity.ok(map.getMap());
    }
}
