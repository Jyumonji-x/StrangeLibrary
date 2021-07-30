package se24.violationservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se24.violationservice.domain.ReserveOverdue;
import se24.violationservice.repository.OverdueRepository;
import se24.violationservice.service.OverdueService;

import java.util.Date;

@RestController
public class OverdueController {
    private final OverdueRepository overdueRepository;
    private final OverdueService overdueService;

    @Autowired
    public OverdueController(OverdueRepository overdueRepository,OverdueService overdueService) {
        this.overdueRepository = overdueRepository;
        this.overdueService = overdueService;
    }

    @PutMapping("/api/overdue/add")
    public ResponseEntity<?> add(
            @RequestBody ReserveOverdue reserveOverdue
    ) {
        Date now = new Date();
        reserveOverdue.setTime(now);
        overdueRepository.save(reserveOverdue);
        return ResponseEntity.ok("存了");
    }

    @GetMapping("/api/overdue/{username}")
    public ResponseEntity<?> getOverdue(@PathVariable String username) {
        System.out.println("SubscribeOverdueController GET /api/overdue:");
        System.out.println("username = " + username);
        return ResponseEntity.ok(overdueService.getOverdue(username).getMap());
    }

    @PostMapping("/api/overdue/{username}")
    public ResponseEntity<?> deleteOverdue(@PathVariable String username) {
        System.out.println("SubscribeOverdueController POST /api/overdue:");
        System.out.println("username = " + username);
        return ResponseEntity.ok(overdueService.deleteOverdue(username).getMap());
    }
}
