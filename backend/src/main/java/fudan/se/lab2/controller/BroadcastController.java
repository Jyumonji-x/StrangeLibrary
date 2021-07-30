package fudan.se.lab2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fudan.se.lab2.controller.request.NameTokenRequest;
import fudan.se.lab2.controller.request.TokenRequest;
import fudan.se.lab2.service.BroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BroadcastController {
    private final BroadcastService broadcastService;

    @Autowired
    public BroadcastController(BroadcastService broadcastService) {
        this.broadcastService = broadcastService;
    }

    @PostMapping("/api/broadcast")
    public ResponseEntity<?> broadcast(
            @RequestBody TokenRequest request
    ) throws JsonProcessingException {
        System.out.println("BroadcastController /api/broadcast");
        System.out.println(request);
        return ResponseEntity.ok(broadcastService.broadcast(request).toJson());
    }
}
