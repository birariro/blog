package com.birairo.blog.member.controller;

import com.birairo.blog.common.NoSuchEntityException;
import com.birairo.blog.common.TokenGenerator;
import com.birairo.blog.member.domain.Member;
import com.birairo.blog.member.service.support.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final TokenGenerator tokenGenerator;

    @PostMapping("/login")
    ResponseEntity login(@RequestBody LoginRequest loginRequest) {

        Member member = memberRepository.findByLoginId(loginRequest.id())
                .orElseThrow(() -> new NoSuchEntityException("not found member"));

        if (!passwordEncoder.matches(loginRequest.pw(), member.getLoginPw())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String jwt = tokenGenerator.generateToken(member.getLoginId());
        return ResponseEntity.ok().body(Map.of(
                "token", jwt
        ));
    }

    @GetMapping("/token/{token}/valid")
    ResponseEntity valid(@PathVariable("token") String token) {
        boolean valid = tokenGenerator.validateToken(token);
        return ResponseEntity.ok().body(
                Map.of("valid", valid)
        );
    }

}
