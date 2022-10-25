package com.example.jwt.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MemberDto {
    private long id;
    private String username;
    private String password;
    private String roles;

    public MemberDto(long id, String username, String password, String roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Member toEntity(){
        return Member.builder()
                        .id(id)
                        .username(username)
                        .password(password)
                        .roles(roles)
                        .build();
    }
}
