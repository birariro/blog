package com.birairo.blog.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class Nickname {
    @Column(name = "nickname", length = 100, nullable = false, unique = true)
    private String value;

    public static Nickname of(String value) {
        Assert.hasText(value, "can not be empty");
        return new Nickname(value);
    }
}
