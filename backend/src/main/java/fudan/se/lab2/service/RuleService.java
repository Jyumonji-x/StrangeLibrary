package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.UpdateRulesRequest;
import fudan.se.lab2.domain.BorrowRule;
import fudan.se.lab2.repository.RuleRepository;
import fudan.se.lab2.util.JsonMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleService {
    private final RuleRepository ruleRepository;

    @Autowired
    public RuleService(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public JsonMap getRules() {
        JsonMap map = new JsonMap();
        String message = "查询规则成功";
        int rtn = 1;

        BorrowRule teacherRule = ruleRepository.findBorrowRuleByIdentity("教师");
        BorrowRule undergraduateRule = ruleRepository.findBorrowRuleByIdentity("本科生");
        BorrowRule postgraduateRule = ruleRepository.findBorrowRuleByIdentity("研究生");

        map.put("maxAmountTeacher", teacherRule.getMaxBorrowNumber());
        map.put("borrowTimeTeacher", teacherRule.getBorrowValidPeriod());
        map.put("subscribeTimeTeacher", teacherRule.getSubscribeValidPeriod());
        map.put("maxAmountUndergra", undergraduateRule.getMaxBorrowNumber());
        map.put("borrowTimeUndergra", undergraduateRule.getBorrowValidPeriod());
        map.put("subscribeTimeUndergra", undergraduateRule.getSubscribeValidPeriod());
        map.put("maxAmountPostgra", postgraduateRule.getMaxBorrowNumber());
        map.put("borrowTimePostgra", postgraduateRule.getBorrowValidPeriod());
        map.put("subscribeTimePostgra", postgraduateRule.getSubscribeValidPeriod());

        map.put("rtn", rtn);
        map.put("message", message);
        return map;
    }

    public JsonMap updateRules(UpdateRulesRequest request) {
        JsonMap map = new JsonMap();
        String message = "修改规则成功";

        BorrowRule teacherRule = ruleRepository.findBorrowRuleByIdentity("教师");
        BorrowRule undergraduateRule = ruleRepository.findBorrowRuleByIdentity("本科生");
        BorrowRule postgraduateRule = ruleRepository.findBorrowRuleByIdentity("研究生");

        teacherRule.setMaxBorrowNumber(request.getMaxAmountTeacher());
        teacherRule.setBorrowValidPeriod(request.getBorrowTimeTeacher());
        teacherRule.setSubscribeValidPeriod(request.getSubscribeTimeTeacher());

        undergraduateRule.setMaxBorrowNumber(request.getMaxAmountUndergra());
        undergraduateRule.setBorrowValidPeriod(request.getBorrowTimeUndergra());
        undergraduateRule.setSubscribeValidPeriod(request.getSubscribeTimeUndergra());

        postgraduateRule.setMaxBorrowNumber(request.getMaxAmountPostgra());
        postgraduateRule.setBorrowValidPeriod(request.getBorrowTimePostgra());
        postgraduateRule.setSubscribeValidPeriod(request.getSubscribeTimePostgra());

        ruleRepository.save(teacherRule);
        ruleRepository.save(undergraduateRule);
        ruleRepository.save(postgraduateRule);

        map.put("rtn", 1);
        map.put("message", message);
        return map;
    }
}
