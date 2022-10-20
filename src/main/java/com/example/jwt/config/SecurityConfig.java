package com.example.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.example.jwt.domain.Role;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private final CorsConfig corsConfig;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().addFilter(corsConfig.corsFilter());
        httpSecurity.formLogin().disable();
        httpSecurity.httpBasic().disable();
        httpSecurity.authorizeRequests(authorize -> 
                                       authorize.antMatchers("/h2-console/**").permitAll()
                                       .antMatchers("/api/v1/user/**")
                                       .hasAnyRole(Role.USER.name(),Role.ADMIN.name(), Role.MANAGER.name())
                                       .antMatchers("/api/v1/manager/**")
                                       .hasAnyRole(Role.ADMIN.name(), Role.MANAGER.name())
                                       .antMatchers("/api/v1/admin/**")
                                       .hasRole(Role.ADMIN.name())
                                       );      
        httpSecurity.headers().frameOptions().disable();
        httpSecurity.csrf().ignoringAntMatchers("/h2-console/**").disable();

        return httpSecurity.build();
    }

}
