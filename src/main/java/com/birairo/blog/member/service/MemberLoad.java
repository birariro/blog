package com.birairo.blog.member.service;

import com.birairo.blog.vo.Nickname;
import com.birairo.blog.vo.VoWithId;

import java.util.List;
import java.util.UUID;

public interface MemberLoad {
    Nickname getMemberNickName(UUID id);
    List<VoWithId<Nickname, UUID>> getMemberNickName(List<UUID> ids);
}
