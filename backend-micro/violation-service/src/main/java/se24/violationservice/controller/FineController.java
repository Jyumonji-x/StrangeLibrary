package se24.violationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se24.violationservice.domain.Fine;
import se24.violationservice.repository.FineRepository;
import se24.violationservice.service.FineService;


@RestController
public class FineController {
    private final FineRepository fineRepository;
    private final FineService fineService;

    @Autowired
    public FineController(FineRepository fineRepository,FineService fineService) {this.fineRepository = fineRepository;
        this.fineService = fineService;
    }

    @PutMapping("/api/fine/add")
    public ResponseEntity<?> add(
            @RequestBody Fine fine
    ){
        // 添加一个罚单
        fineRepository.save(fine);
        return ResponseEntity.ok("");
    }

    @GetMapping("/api/payment/fine/{username}")
    public ResponseEntity<?> getFine(
            @PathVariable String username) {
        System.out.println("FineController GET /api/payment/fine/{username}");
        System.out.println("username = " + username);
        return ResponseEntity.ok(fineService.getFine(username).getMap());
    }

    @PostMapping("/api/payment/fine/{username}")
    public ResponseEntity<?> payFine(
            @PathVariable String username) {
        System.out.println("FineController POST /api/payment/fine/{username}");
        System.out.println("username = " + username);
        return ResponseEntity.ok(fineService.payFine(username).getMap());
    }


}
