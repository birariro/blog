package com.birairo.blog.member.service.support;

import com.birairo.blog.common.NoSuchEntityException;
import com.birairo.blog.member.service.MemberLoad;
import com.birairo.blog.vo.Nickname;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberLoader implements MemberLoad {
    private final MemberRepository memberRepository;

    @Override
    public Nickname getMemberNickName(UUID id) {

        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("not found author"))
                .getNickname();
    }

}
