package com.example.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwt.domain.MemberDto;
import com.example.jwt.domain.Role;
import com.example.jwt.repository.MemberRepository;

@RestController
public class RestApiController {
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MemberRepository memberRepository;


    @GetMapping("home")
    public String home(){
        return "<h1>home</h1>";
    }

    @PostMapping("/join")
    public String join( @RequestBody MemberDto memberDto){
        memberDto.setPassword(bCryptPasswordEncoder.encode(memberDto.getPassword()));
        memberDto.setRoles(Role.USER.role());
        memberRepository.save(memberDto.toEntity());
        return "회원가입 완료";
    }

    @GetMapping("/api/v1/user")
    public String user(){
        return "user";
    }

    @GetMapping("/api/v1/admin")
    public String admin(){
        return "admin";
    }

    @GetMapping("/api/v1/manager")
    public String manager(){
        return "manager";
    }

}
