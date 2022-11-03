package com.example.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.example.jwt.config.jwt.JwtAuthenticationFilter;
import com.example.jwt.config.jwt.JwtAuthorizationFilter;
import com.example.jwt.config.jwt.JwtProvider;
import com.example.jwt.domain.Role;
import com.example.jwt.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private final CorsConfig corsConfig;

    @Autowired
    private final MemberRepository memberRepository;

    private final JwtProvider jwtProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and().addFilter(corsConfig.corsFilter());
        httpSecurity.formLogin().disable();
        httpSecurity.httpBasic().disable().apply(new MyCustomDsl());
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

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity>{
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

            http.addFilter(new JwtAuthenticationFilter(authenticationManager, jwtProvider))
                .addFilter(new JwtAuthorizationFilter(authenticationManager, memberRepository, jwtProvider));
        }
    }

}
