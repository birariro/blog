package com.birairo.blog.member.service;

import com.birairo.blog.member.domain.Member;

public interface CreateIfNecessaryGuestLoad {
    Member getGuest(String ip);
}
