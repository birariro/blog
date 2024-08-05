package com.birairo.blog.member;

import com.birairo.blog.member.service.support.NicknameGenerator;
import com.birairo.blog.vo.Nickname;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NicknameGeneratorTest {

    private NicknameGenerator sut = new NicknameGenerator();

    @Test
    void should_random_nickname() {

        List<String> nicknames = new ArrayList();
        int limit = 1000;
        for (int i = 0; i < limit; i++) {
            Nickname nickname = sut.generateNickname();
            nicknames.add(nickname.getValue());
            System.out.println(nickname);
        }

        assertThat(nicknames.size())
                .isEqualTo(new HashSet<>(nicknames).size());
    }

}