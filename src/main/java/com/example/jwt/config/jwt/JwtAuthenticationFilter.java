package com.example.jwt.config.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt.config.auth.PrincipalDetails;
import com.example.jwt.domain.Member;
import com.example.jwt.domain.MemberDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
       try {
        ObjectMapper objectMapper = new ObjectMapper();
        MemberDto member = objectMapper.readValue(request.getInputStream(), MemberDto.class);
        UsernamePasswordAuthenticationToken authenticationToken =
                                            new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword());
        
        Authentication authentication =
                                authenticationManager.authenticate(authenticationToken);
                                
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();     

        return authentication;

       } catch (Exception e) {
        e.printStackTrace();
       }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
                
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        
        // TODO:jwt sign
        String jwtToken = JWT.create()
                            .withSubject("token")
                            .withExpiresAt(new Date(System.currentTimeMillis()+(60000*10)))
                            .withClaim("id", principalDetails.getMember().getId())
                            .withClaim("username", principalDetails.getMember().getUsername())
                            .sign(Algorithm.HMAC512("cos"));
       
        response.addHeader("Authorization", "Bearer "+jwtToken);                    
    }
    
}
