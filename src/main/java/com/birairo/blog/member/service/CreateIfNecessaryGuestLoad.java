package com.birairo.blog.member.service;

import com.birairo.blog.member.domain.Guest;

public interface CreateIfNecessaryGuestLoad {
    Guest getGuest(String ip);
}
