package com.birairo.blog.member.service.support;

import com.birairo.blog.common.NoSuchEntityException;
import com.birairo.blog.member.domain.Guest;
import com.birairo.blog.member.service.MemberLoad;
import com.birairo.blog.vo.Nickname;
import com.birairo.blog.vo.VoWithId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberLoader implements MemberLoad {
    private final GuestRepository guestRepository;
    private final MemberRepository memberRepository;

    @Override
    public Nickname getMemberNickName(UUID id) {

        Optional<Guest> guest = guestRepository.findById(id);
        if (guest.isPresent()) {
            return guest.get().getNickname();
        }
        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("not found author"))
                .getNickname();


    }

    @Override
    public List<VoWithId<Nickname, UUID>> getMemberNickName(List<UUID> ids) {
        return guestRepository.findByIdIn(ids)
                .stream()
                .map(guest -> new VoWithId<>(guest.getNickname(), guest.getId()))
                .toList();

    }
}
