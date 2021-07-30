package fudan.se.lab2.security;

import fudan.se.lab2.security.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author LBW
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public SecurityConfig( JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // TODO: Configure your auth here. Remember to read the JavaDoc carefully.
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO: you need to configure your http security. Remember to read the JavaDoc carefully.

        // We don't need CSRF for this project.
        //放行所有请求
        http.csrf().disable()
                .authorizeRequests()
                .anyRequest().permitAll();

//      Here we use JWT(Json Web Token) to authenticate the user.
//      You need to write your code in the class 'JwtRequestFilter' to make it works.
        //http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Hint: Now you can view h2-console page at `http://IP-Address:<port>/h2-console` without authentication.
        web.ignoring().antMatchers("/h2-console/**");
    }


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
