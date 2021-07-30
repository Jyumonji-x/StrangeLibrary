package fudan.se.lab2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fudan.se.lab2.service.OverdueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscribeOverdueController {
    private final OverdueService service;

    @Autowired
    public SubscribeOverdueController(OverdueService service) {
        this.service = service;
    }

    @GetMapping("/api/overdue/{username}")
    public ResponseEntity<?> getOverdue(@PathVariable String username) throws JsonProcessingException {
        System.out.println("SubscribeOverdueController GET /api/overdue:");
        System.out.println("username = " + username);
        return ResponseEntity.ok(service.getOverdue(username).toJson());
    }

    @PostMapping("/api/overdue/{username}")
    public ResponseEntity<?> deleteOverdue(@PathVariable String username) throws JsonProcessingException {
        System.out.println("SubscribeOverdueController POST /api/overdue:");
        System.out.println("username = " + username);
        return ResponseEntity.ok(service.deleteOverdue(username).toJson());
    }
}
