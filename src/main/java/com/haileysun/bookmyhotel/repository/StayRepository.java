package com.haileysun.bookmyhotel.repository;

import com.haileysun.bookmyhotel.entity.Stay;
import com.haileysun.bookmyhotel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StayRepository extends JpaRepository<Stay, Long> {
    // 1. Find list of stays by host's username
    List<Stay> findByHost(User user);
}

