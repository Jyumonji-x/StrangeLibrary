package fudan.se.lab2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fudan.se.lab2.controller.request.*;
import fudan.se.lab2.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BorrowController {
    private final BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/api/borrow/borrowed_subscribed_books")
    public ResponseEntity<?> borrowed(@RequestBody NameTokenRequest request) throws JsonProcessingException {
        System.out.print("BorrowController /api/borrow/borrowed_subscribed_books:");
        System.out.println("Param: user_name = " + request.getUserName());
        System.out.println("Param: token = " + request.getToken());
        return ResponseEntity.ok(borrowService.borrowed(request.getUserName(), request.getToken()).toJson());
    }

    // 预约书籍
    @PostMapping("/api/borrow/subscribe")
    public ResponseEntity<?> reserve(
            @RequestBody TokenDuplicateRequest request
    ) throws JsonProcessingException {
        System.out.print("BorrowController /api/borrow/subscribe:");
        System.out.println("Param: duplicate_id = " + request.getDuplicate_id());
        System.out.println("Param: token = " + request.getToken());
        return ResponseEntity.ok(borrowService.subscribeBook(request).toJson());
    }

    // 现场还书
    @PostMapping("/api/borrow/onsiteReturn")
    public ResponseEntity<?> returnBook(
            @RequestBody TokenDuplicateBranchStateRequest request
    ) throws JsonProcessingException {
        System.out.print("BorrowController /api/borrow/onsiteReturn:");
        System.out.println("Param: duplicate_id = " + request.getDuplicate_id());
        System.out.println("Param: token = " + request.getToken());
        System.out.println("Param: branch = " + request.getBranch());
        return ResponseEntity.ok(borrowService.returnBook(request).toJson());
    }

    // 现场取预约
    @PostMapping("/api/borrow/getSubscribe")
    public ResponseEntity<?> getSubscribe(
            @RequestBody NameTokenDuplicateBranchRequest request
    ) throws JsonProcessingException {
        System.out.print("BorrowController /api/borrow/subscribe:");
        System.out.println("Param: duplicate_id = " + request.getDuplicate_id());
        System.out.println("Param: token = " + request.getToken());
        System.out.println("Param: borrow_name = " + request.getBorrow_name());
        System.out.println("Param: branch = " + request.getBranch());
        return ResponseEntity.ok(borrowService.getSubscribeBook(request).toJson());
    }

    // 现场借书
    @PostMapping("/api/borrow/onsiteBorrow")
    public ResponseEntity<?> borrowBook(
            @RequestBody NameTokenDuplicateBranchRequest request
    ) throws JsonProcessingException {
        System.out.print("BorrowController /api/borrow/subscribe:");
        System.out.println("Param: duplicate_id = " + request.getDuplicate_id());
        System.out.println("Param: token = " + request.getToken());
        System.out.println("Param: borrow_name = " + request.getBorrow_name());
        System.out.println("Param: branch = " + request.getBranch());
        return ResponseEntity.ok(borrowService.borrowBook(request).toJson());
    }

    @GetMapping("/api/history/username/{username}")
    public ResponseEntity<?> getHistoryByUsername(@PathVariable String username) throws JsonProcessingException {
        System.out.println("BorrowController /api/history/username:");
        System.out.println("username = " + username);
        return ResponseEntity.ok(borrowService.getHistoryByUsername(username).toJson());
    }

    @GetMapping("/api/history/duplicateId/{duplicateId}")
    public ResponseEntity<?> getHistoryByDuplicateId(@PathVariable String duplicateId) throws JsonProcessingException {
        System.out.println("BorrowController /api/history/username:");
        System.out.println("duplicateId = " + duplicateId);
        return ResponseEntity.ok(borrowService.getHistoryByDuplicateId(duplicateId).toJson());
    }

    @GetMapping("/api/history")
    public ResponseEntity<?> getHistory() throws JsonProcessingException {
        System.out.println("BorrowController /api/history:");
        return ResponseEntity.ok(borrowService.getHistory().toJson());
    }
}
