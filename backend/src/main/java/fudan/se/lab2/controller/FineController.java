package fudan.se.lab2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fudan.se.lab2.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FineController {
    private final FineService service;


    @Autowired
    public FineController(FineService service) {
        this.service = service;
    }

    @GetMapping("/api/payment/fine/{username}")
    public ResponseEntity<?> getFine(
            @PathVariable String username) throws JsonProcessingException {
        System.out.println("FineController GET /api/payment/fine/{username}");
        System.out.println("username = " + username);
        return ResponseEntity.ok(service.getFine(username).toJson());
    }

    @PostMapping("/api/payment/fine/{username}")
    public ResponseEntity<?> payFine(
            @PathVariable String username) throws JsonProcessingException {
        System.out.println("FineController POST /api/payment/fine/{username}");
        System.out.println("username = " + username);
        return ResponseEntity.ok(service.payFine(username).toJson());
    }
}
