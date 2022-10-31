package com.example.jwt.config.jwt;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt.config.auth.PrincipalDetails;

@Component
public class JwtProvider {
    
    @Value("${jwt.secret_key}")
    private String SECRET_KEY;

    @Value("${jwt.expiration_time}")
    private int EXPIRATION_TIME;

    public static String TOKEN_PREFIX = "Bearer ";

    public static String HEADER_STRING = "Authorization";

    public String createToken(PrincipalDetails principalDetails){

        String jwtToekn = JWT.create()
                                .withSubject("token")
                                .withExpiresAt(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                                .withClaim("id", principalDetails.getMember().getId())
                                .withClaim("username", principalDetails.getMember().getUsername())
                                .sign(Algorithm.HMAC512(SECRET_KEY));

        return jwtToekn;
    }

    public String getUserName(String jwtToken){
        String username = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                        .build().verify(jwtToken)
                        .getClaim("username").asString();
        return username;
    }

    public String resolveToken(HttpServletRequest request){
        String jwtHeader = request.getHeader("Authorization");
        
        if(jwtHeader == null || !jwtHeader.startsWith(TOKEN_PREFIX)){
            return null;
        }else{
            return jwtHeader.replace(TOKEN_PREFIX, "");
        }
    }

}


       