package fudan.se.lab2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fudan.se.lab2.controller.request.UpdateRulesRequest;
import fudan.se.lab2.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RuleController {
    private final RuleService service;

    @Autowired
    public RuleController(RuleService service) {
        this.service = service;
    }

    @GetMapping("/api/borrowRules")
    public ResponseEntity<?> getRules() throws JsonProcessingException {
        System.out.println("RuleController GET /api/borrowRules:");
        return ResponseEntity.ok(service.getRules().toJson());
    }

    @PostMapping("/api/borrowRules")
    public ResponseEntity<?> updateRules(
            @RequestBody UpdateRulesRequest request
    ) throws JsonProcessingException {
        System.out.println("RuleController POST /api/borrowRules:");
        System.out.println(request.toString());
        return ResponseEntity.ok(service.updateRules(request).toJson());
    }

}
