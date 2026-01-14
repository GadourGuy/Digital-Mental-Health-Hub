package com.secj3303.security;

import java.util.Collections;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.secj3303.dao.user.UserDao;
import com.secj3303.model.User;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDao userDao;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Simple, standard BCrypt encoder. 
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            User user = userDao.getUserByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()))
            );
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authenticationProvider(authenticationProvider())
            .authorizeRequests()
                .antMatchers("/login", "/signup", "/css/**", "/js/**", "/images/**", "/resources/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/student/**").hasRole("STUDENT")
                .antMatchers("/professional/**").hasRole("PROFESSIONAL")
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(myAuthenticationSuccessHandler()) // <--- THIS IS THE FIX
                .failureUrl("/login?error=true")
                .permitAll()
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            
            // 1. Get the logged-in email
            String email = authentication.getName();
            
            // 2. Fetch the FULL User object
            User user = userDao.getUserByEmail(email);
            
            // 3. Save User object AND Role String to session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            
            
            session.setAttribute("role", user.getRole()); 

            // 4. Redirect based on Role
            for (GrantedAuthority auth : authentication.getAuthorities()) {
                String role = auth.getAuthority();
                if (role.equals("ROLE_STUDENT")) {
                    response.sendRedirect(request.getContextPath() + "/student/home");
                    return;
                } else if (role.equals("ROLE_PROFESSIONAL")) {
                    response.sendRedirect(request.getContextPath() + "/professional/home");
                    return;
                } else if (role.equals("ROLE_ADMIN")) {
                    response.sendRedirect(request.getContextPath() + "/admin/home");
                    return;
                }
            }
            response.sendRedirect(request.getContextPath() + "/home");
        };
    }
}