package com.birairo.blog.aggregate.service.support;

import com.birairo.blog.aggregate.domain.Counter;
import com.birairo.blog.vo.Parent;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CounterRepository extends Repository<Counter, UUID> {

    boolean existsByParentId(Parent parent);

    Optional<Counter> findFirstByParentId(Parent parent);

    @Modifying
    @Query("UPDATE Counter c SET c.count = c.count + 1 WHERE c.parentId = :parentId")
    int incrementCountByParentId(@Param("parentId") Parent parentId);


    Counter save(Counter guest);

}
