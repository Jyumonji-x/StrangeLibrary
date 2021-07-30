package fudan.se.lab2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author LBW
 */
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                //TODO: If you encounter some Cross-Domain problems（跨域问题）, Maybe you can do something here.
                registry.addMapping("/**")  // 可限制哪个请求可以通过跨域
                        .allowedHeaders("*")  // 可限制固定请求头可以通过跨域
                        .allowedMethods("*") // 可限制固定methods可以通过跨域
                        .allowedOrigins("*")  // 可限制访问ip可以通过跨域
                        .allowCredentials(true) // 是否允许发送cookie
                        .exposedHeaders(HttpHeaders.SET_COOKIE);
            }
        };
    }
}
