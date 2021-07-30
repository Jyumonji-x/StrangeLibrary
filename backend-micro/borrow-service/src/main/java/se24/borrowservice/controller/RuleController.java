package se24.borrowservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import se24.borrowservice.controller.request.UpdateRulesRequest;
import se24.borrowservice.domain.Rule;
import se24.borrowservice.repository.RuleRepository;
import se24.borrowservice.service.RuleService;
import se24.borrowservice.tool.ReturnMap;

@RestController
public class RuleController {
    private final RuleService ruleService;
    private final RuleRepository ruleRepository;

    @Autowired
    public RuleController(RuleService ruleService, RuleRepository ruleRepository) {
        this.ruleService = ruleService;
        this.ruleRepository = ruleRepository;
    }

    @GetMapping("/api/borrowRules")
    public ResponseEntity<?> getRules() {
        System.out.println("RuleController GET /api/borrowRules:");
        ReturnMap map = new ReturnMap();
        Rule teacherRule = ruleRepository.findRuleByIdentity("教师");
        Rule undergraduateRule = ruleRepository.findRuleByIdentity("本科生");
        Rule postgraduateRule = ruleRepository.findRuleByIdentity("研究生");
        map.put("maxAmountTeacher", teacherRule.getBorrowMaxNum());
        map.put("borrowTimeTeacher", teacherRule.getBorrowValidTime());
        map.put("subscribeTimeTeacher", teacherRule.getReserveValidTime());
        map.put("maxAmountUndergra", undergraduateRule.getBorrowMaxNum());
        map.put("borrowTimeUndergra", undergraduateRule.getBorrowValidTime());
        map.put("subscribeTimeUndergra", undergraduateRule.getReserveValidTime());
        map.put("maxAmountPostgra", postgraduateRule.getBorrowMaxNum());
        map.put("borrowTimePostgra", postgraduateRule.getBorrowValidTime());
        map.put("subscribeTimePostgra", postgraduateRule.getReserveValidTime());
        map.setRtn(1);
        map.setMessage("规则查询成功");
        return ResponseEntity.ok(map.getMap());
    }

    @PostMapping("/api/borrowRules")
    public ResponseEntity<?> updateRules(
            @RequestBody UpdateRulesRequest request
    ) {
        System.out.println("RuleController POST /api/borrowRules:");
        System.out.println(request.toString());
        Rule teacherRule = ruleRepository.findRuleByIdentity("教师");
        Rule undergraduateRule = ruleRepository.findRuleByIdentity("本科生");
        Rule postgraduateRule = ruleRepository.findRuleByIdentity("研究生");
        teacherRule.setBorrowMaxNum(request.getMaxAmountTeacher());
        teacherRule.setBorrowValidTime(request.getBorrowTimeTeacher());
        teacherRule.setReserveValidTime(request.getSubscribeTimeTeacher());
        undergraduateRule.setBorrowMaxNum(request.getMaxAmountUndergra());
        undergraduateRule.setBorrowValidTime(request.getBorrowTimeUndergra());
        undergraduateRule.setReserveValidTime(request.getSubscribeTimeUndergra());
        postgraduateRule.setBorrowMaxNum(request.getMaxAmountPostgra());
        postgraduateRule.setBorrowValidTime(request.getBorrowTimePostgra());
        postgraduateRule.setReserveValidTime(request.getSubscribeTimePostgra());
        ruleRepository.save(teacherRule);
        ruleRepository.save(undergraduateRule);
        ruleRepository.save(postgraduateRule);

        ReturnMap map=new ReturnMap();
        map.setRtn(1);
        map.setMessage("修改规则成功");
        return ResponseEntity.ok(map.getMap());
    }
}
