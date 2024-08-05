package com.birairo.blog.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Title {
    
    @Column(name = "title", length = 100, unique = true, nullable = false)
    private String value;

    public static Title of(String value) {
        Assert.hasText(value, "can not be empty");
        return new Title(value);
    }
}
