package com.example.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jwt.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
    Member findByUsername(String username);
    
}
