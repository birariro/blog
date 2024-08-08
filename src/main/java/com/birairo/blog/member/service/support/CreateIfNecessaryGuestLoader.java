package com.birairo.blog.member.service.support;

import com.birairo.blog.member.domain.Member;
import com.birairo.blog.member.service.CreateIfNecessaryGuestLoad;
import com.birairo.blog.member.service.NicknameGenerate;
import com.birairo.blog.vo.Nickname;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateIfNecessaryGuestLoader implements CreateIfNecessaryGuestLoad {

    private final MemberRepository repository;
    private final NicknameGenerate nicknameGenerator;

    @Transactional
    public Member getGuest(String ip) {

        if (repository.existsByIp(ip)) {
            return repository.findFirstByIp(ip)
                    .orElseThrow();
        }

        Nickname nickname = nicknameGenerator.generateNickname();
        Member guest = repository.save(Member.ofGuest(ip, nickname));

        log.info(String.format("new create guest nickname: ", nickname));
        return guest;
    }

}
