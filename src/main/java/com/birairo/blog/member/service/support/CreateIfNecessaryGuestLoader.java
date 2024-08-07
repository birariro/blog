package com.birairo.blog.member.service.support;

import com.birairo.blog.member.domain.Guest;
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

    private final GuestRepository guestRepository;
    private final NicknameGenerate nicknameGenerator;

    @Transactional
    public Guest getGuest(String ip) {

        if (guestRepository.existsByIp(ip)) {
            return guestRepository.findFirstByIp(ip)
                    .orElseThrow();
        }

        Nickname nickname = nicknameGenerator.generateNickname();
        Guest guest = guestRepository.save(new Guest(ip, nickname));

        log.info(String.format("new create guest nickname: ", nickname));
        return guest;
    }

}
