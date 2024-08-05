package com.birairo.blog.member.domain;

import com.birairo.blog.common.UpdatableDomain;
import com.birairo.blog.vo.Nickname;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = true)
public class Member extends UpdatableDomain {

    private String loginId;
    private String loginPw;
    @Embedded
    private Nickname nickname;

    public Member(String loginId, String loginPw, Nickname nickname) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.nickname = nickname;
    }
}
