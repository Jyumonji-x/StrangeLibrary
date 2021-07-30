package se24.userservice;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import se24.userservice.domain.User;
import se24.userservice.repository.UserRepository;
import se24.userservice.tool.SessionMap;

import java.util.Date;

@SpringBootApplication
public class UserServiceApplication {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }


    @Bean
    public CommandLineRunner dataLoader() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) {
                // 整个超管账号！顺便让它登录，拿到session
                User user=new User();
                user.setUsername("admin");
                user.setPassword("admin");
                user.setTime_create(new Date());
                user.setPermission("超级管理员");
                if (userRepository.findUserByUsername("admin") == null) {
                    userRepository.save(user);
                }
                String superAdminSession = SessionMap.login(user);
                System.out.println("superAdminSession = " + superAdminSession);
                // 再整个普通账号登录
                user=new User();
                user.setUsername("19302010020");
                user.setPassword("abcd1234");
                user.setTime_create(new Date());
                user.setTime_login(new Date());
                user.setPermission("普通用户");
                user.setIdentity("本科生");
                user.setCredit(100);
                if (userRepository.findUserByUsername("19302010020") == null) {
                    userRepository.save(user);
                }
                String session = SessionMap.login(user);
            }
        };
    }
}
