package fudan.se.lab2;

import fudan.se.lab2.domain.BorrowRule;
import fudan.se.lab2.domain.User;
import fudan.se.lab2.repository.RuleRepository;
import fudan.se.lab2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Date;

/**
 * Welcome to 2021 Software Engineering Lab2.
 * This is your first lab to write your own code and build a spring boot application.
 * Enjoy it :)
 *
 * @author LBW
 */
@SpringBootApplication
public class StrangeLibraryApplication {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RuleRepository ruleRepository;

    public StrangeLibraryApplication(UserRepository userRepository, RuleRepository ruleRepository) {
        this.userRepository = userRepository;
        this.ruleRepository = ruleRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(StrangeLibraryApplication.class, args);
    }

    /**
     * This is a function to create some basic entities when the application starts.
     * Now we are using a In-Memory database, so you need it.
     * You can change it as you like.
     */
    @Bean
    public CommandLineRunner dataLoader() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) {
                if (userRepository.findByUsername("admin") == null) {
                    userRepository.save(new User("admin", "admin", "admin.fudan.edu.cn", 2, new Date(), new Date(), ""));
                }
                if (ruleRepository.findBorrowRuleByIdentity("本科生") == null) {
                    ruleRepository.save(new BorrowRule("本科生", 5, 30, 10));
                }
                if (ruleRepository.findBorrowRuleByIdentity("研究生") == null) {
                    ruleRepository.save(new BorrowRule("研究生", 10, 30, 10));
                }
                if (ruleRepository.findBorrowRuleByIdentity("教师") == null) {
                    ruleRepository.save(new BorrowRule("教师", 20, 60, 20));
                }
            }
        };
    }
}