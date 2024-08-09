package com.birairo.blog.member.domain;

import com.birairo.blog.common.UpdatableDomain;
import com.birairo.blog.vo.Nickname;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Column(name = "login_id")
    private String loginId;
    @Column(name = "login_pw")
    private String loginPw;
    @Column(unique = true)
    private String ip;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private MemberType type;
    @Embedded
    private Nickname nickname;

    public Member(String loginId, String loginPw, String ip, MemberType type, Nickname nickname) {
        this.loginId = loginId;
        this.loginPw = loginPw;
        this.ip = ip;
        this.type = type;
        this.nickname = nickname;
    }

    public static Member ofGuest(String ip, Nickname nickname) {
        return new Member(null, null, ip, MemberType.GUEST, nickname);
    }

    public static Member of(String loginId, String loginPw, Nickname nickname) {
        return new Member(loginId, loginPw, null, MemberType.MEMBER, nickname);
    }
}
