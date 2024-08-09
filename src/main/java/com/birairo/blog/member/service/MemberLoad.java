package com.birairo.blog.member.service;

import com.birairo.blog.vo.Nickname;

import java.util.UUID;

public interface MemberLoad {
    Nickname getMemberNickName(UUID id);
}
