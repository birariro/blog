package com.birairo.blog.common;

import jakarta.persistence.Column;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

public class UpdatableDomain extends Domain {
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
