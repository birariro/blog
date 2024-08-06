package com.birairo.blog.member.controller;

import com.birairo.blog.common.NoSuchEntityException;
import com.birairo.blog.common.TokenGenerator;
import com.birairo.blog.member.domain.Member;
import com.birairo.blog.member.service.support.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenGenerator tokenGenerator;

    @PostMapping("/login")
    ResponseEntity login(@RequestBody LoginRequest loginRequest) {

        Member member = memberRepository.findByLoginId(loginRequest.id())
                .orElseThrow(() -> new NoSuchEntityException("not found member"));

        //todo 적용 필요
        //if (!passwordEncoder.matches(loginRequest.pw(), member.getLoginPw())) {
        if (!loginRequest.pw().equals(member.getLoginPw())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String jwt = tokenGenerator.generateToken(member.getLoginId());
        return ResponseEntity.ok().body(Map.of(
                "token", jwt
        ));
    }

}
