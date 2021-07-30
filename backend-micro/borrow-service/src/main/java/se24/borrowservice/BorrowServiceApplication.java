package se24.borrowservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import se24.borrowservice.domain.Rule;
import se24.borrowservice.repository.RuleRepository;

import java.util.Date;

@SpringBootApplication
public class BorrowServiceApplication {

    private final RuleRepository ruleRepository;

    @Autowired
    public BorrowServiceApplication(RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(BorrowServiceApplication.class, args);
    }

    // 整点默认规则
    @Bean
    public CommandLineRunner dataLoader() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) {
                Rule rule=new Rule();
                rule.setIdentity("本科生");
                rule.setReserveValidTime(30);
                rule.setBorrowValidTime(30);
                rule.setBorrowMaxNum(5);

                if (!ruleRepository.existsByIdentity("本科生")){
                    ruleRepository.saveAndFlush(rule);
                }
                if (!ruleRepository.existsByIdentity("研究生")){
                    rule.setIdentity("研究生");
                    ruleRepository.saveAndFlush(rule);
                }
                if (!ruleRepository.existsByIdentity("教师")){
                    rule.setIdentity("教师");
                    ruleRepository.saveAndFlush(rule);
                }
            }
        };
    }
}
