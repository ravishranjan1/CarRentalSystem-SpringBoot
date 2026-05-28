package com.rentify.carrental.auth;

import com.rentify.carrental.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/rentify/login", "/rentify/register","/rentify/user-create","/css/**","/js/**","/images/**").permitAll()
                        .requestMatchers("/rentify/admin/**")
                        .hasAnyRole(Role.ADMIN.getRoleName())
                        .requestMatchers("/rentify/user/**")
                        .hasAnyRole(Role.USER.getRoleName())
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/rentify/login")
                        .loginProcessingUrl("/rentify/login")
                        .defaultSuccessUrl("/rentify/", true)
                        .failureUrl("/rentify/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/rentify/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/rentify/access-denied")
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
