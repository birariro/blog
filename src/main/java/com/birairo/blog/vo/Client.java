package com.birairo.blog.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Client {
    private Nickname nickname;
    private boolean accreditation;

    public static Client of(Nickname nickname, boolean accreditation) {
        Assert.notNull(nickname, "can not be null");
        return new Client(nickname, accreditation);
    }
}
