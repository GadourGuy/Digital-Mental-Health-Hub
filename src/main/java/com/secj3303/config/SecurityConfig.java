package com.secj3303.config; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF to allow your Chatbot POST requests to work
            .csrf().disable()
            
            // 2. Allow all requests so your existing manual login logic (session checks) still works
            .authorizeRequests()
                .antMatchers("/**").permitAll()
            .and()
            
            // 3. Disable the default Spring Security login page
            .formLogin().disable()
            .logout().disable();
            
        return http.build();
    }
}