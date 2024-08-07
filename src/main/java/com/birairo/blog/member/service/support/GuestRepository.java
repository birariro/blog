package com.birairo.blog.member.service.support;

import com.birairo.blog.member.domain.Guest;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GuestRepository extends Repository<Guest, UUID> {
    Optional<Guest> findFirstByIp(String ip);

    Optional<Guest> findById(UUID id);

    List<Guest> findByIdIn(List<UUID> id);


    boolean existsByIp(String ip);

    Guest save(Guest guest);
}
