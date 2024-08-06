package com.birairo.blog.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokenGeneratorTest {

    private TokenGenerator sut;

    @BeforeEach
    void init() {
        this.sut = new TokenGenerator(
                "TestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKeyTestKey",
                60
        );

    }

    @Test
    void should_valid_success_then_generatedToken() {

        String token = sut.generateToken("test_login_id");
        Assertions.assertThat(sut.validateToken(token)).isTrue();
    }

    @Test
    void should_username_then_generatedToken() {
        String username = "test_login_id";
        String token = sut.generateToken(username);
        Assertions.assertThat(sut.getUsernameFromToken(token)).isEqualTo(username);
    }

    @Test
    void should_valid_fail_then_notGeneratedToken() {

        String token = "ttJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0X2xvZ2luX2lkIiwiaWF0IjoxNzIyOTI0Mjk5LCJleHAiOjE3MjI5ODQyOTl9.cTtfLSNurhdXwwZb0IkKFOPhACPacGV8VeM_wocuT-I";
        Assertions.assertThat(sut.validateToken(token)).isFalse();
    }

}