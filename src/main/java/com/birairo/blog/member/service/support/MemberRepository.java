package com.birairo.blog.member.service.support;

import com.birairo.blog.member.domain.Member;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends Repository<Member, UUID> {
    Optional<Member> findByLoginId(String loginId);
    Optional<Member> findById(UUID id);
}
