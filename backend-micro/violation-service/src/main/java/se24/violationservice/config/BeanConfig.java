package se24.violationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {
    // 用依赖注入方式获取restTemplate，以便测试中注入
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
