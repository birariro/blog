package com.birairo.blog.member.service.support.repository;

import com.birairo.blog.member.domain.Guest;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

public interface GuestRepository extends Repository<Guest, UUID> {

    Optional<Guest> findFirstByIp(String ip);

    boolean existsByIp(String ip);

    Guest save(Guest guest);
}
