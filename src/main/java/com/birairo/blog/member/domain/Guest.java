package com.birairo.blog.member.domain;

import com.birairo.blog.common.UpdatableDomain;
import com.birairo.blog.vo.Nickname;
import jakarta.persistence.Column;
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
public class Guest extends UpdatableDomain {

    @Column(unique = true)
    private String ip;
    @Embedded
    private Nickname nickname;

    public Guest(String ip, Nickname nickname) {
        this.ip = ip;
        this.nickname = nickname;
    }
}
