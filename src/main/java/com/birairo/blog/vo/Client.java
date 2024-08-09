package com.birairo.blog.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Client {
    private UUID id;
    private boolean accreditation;

    public static Client of(UUID id, boolean accreditation) {
        Assert.notNull(id, "can not be null");
        return new Client(id, accreditation);
    }
}
