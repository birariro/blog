package com.birairo.blog.aggregate.domain;

import com.birairo.blog.vo.Parent;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;

import java.util.UUID;

@Entity
@Table(name = "aggregate_count")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class Counter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcType(VarcharJdbcType.class)
    private UUID id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CountType type;

    @Column(unique = true)
    @Embedded
    private Parent parentId;

    @Column(name = "count")
    private long count;

    private Counter(CountType type, Parent parentId, long count) {
        this.type = type;
        this.parentId = parentId;
        this.count = count;
    }

    public static Counter of(Parent parentId) {
        return new Counter(CountType.READ, parentId, 1);
    }
}
