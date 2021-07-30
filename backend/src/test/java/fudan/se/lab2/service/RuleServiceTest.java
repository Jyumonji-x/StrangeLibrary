package fudan.se.lab2.service;

import fudan.se.lab2.controller.request.UpdateRulesRequest;
import fudan.se.lab2.util.JsonMap;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Transactional
@SpringBootTest
class RuleServiceTest {
    @Autowired
    private RuleService ruleService;

    private UpdateRulesRequest request = new UpdateRulesRequest();

    @Test
    void getRulesTest() {
        // 此处取出的是初始数据
        JsonMap rules = ruleService.getRules();
        assertEquals(1,rules.get("rtn"));
        assertEquals(10,rules.get("maxAmountPostgra"));
    }

    @Test
    void updateRules() {
        request.setBorrowTimePostgra(5);
        request.setMaxAmountPostgra(5);
        request.setSubscribeTimePostgra(5);
        request.setMaxAmountTeacher(5);
        request.setBorrowTimeTeacher(5);
        request.setSubscribeTimeTeacher(5);
        request.setMaxAmountUndergra(5);
        request.setBorrowTimeUndergra(5);
        request.setSubscribeTimeUndergra(5);
        JsonMap rules = ruleService.updateRules(request);
        assertEquals(1,rules.get("rtn"));
        assertEquals("修改规则成功",rules.get("message"));
    }

}