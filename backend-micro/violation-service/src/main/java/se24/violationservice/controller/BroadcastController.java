package se24.violationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import se24.violationservice.service.BroadcastService;

@RestController
public class BroadcastController {
    private final BroadcastService broadcastService;

    @Autowired
    public BroadcastController(BroadcastService broadcastService) {
        this.broadcastService = broadcastService;
    }



    @PostMapping("/api/broadcast")
    public ResponseEntity<?> broadcast() {
        System.out.println("群发提醒");


        return ResponseEntity.ok(broadcastService.broadcast().getMap());
    }
}
