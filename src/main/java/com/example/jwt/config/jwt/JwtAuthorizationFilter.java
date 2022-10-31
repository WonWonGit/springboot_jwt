package com.example.jwt.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt.config.auth.PrincipalDetails;
import com.example.jwt.domain.Member;
import com.example.jwt.repository.MemberRepository;

//시큐리티가 필터를 가지고 있는데 그 필터중 basic authenticationFilter 라는게 있다
//권한, 인증이 필요한 특정 주소를 요청할 때 위 필터를 탄다
//만약 권한, 인증이 필요한 주소가 아니면 이 필터를 안탄다
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{

    private MemberRepository memberRepository;

    private JwtProvider jwtProvider;
    
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.memberRepository =  memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String jwtToken = jwtProvider.resolveToken(request);

        
        if(jwtToken == null){
            chain.doFilter(request, response);
            return;
        }
        
        String username = jwtProvider.getUserName(jwtToken);

        if(username != null){

            Member memberEntity = memberRepository.findByUsername(username);

            PrincipalDetails principalDetails =  new PrincipalDetails(memberEntity);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

        }                        

        chain.doFilter(request, response);


    }

    

}
