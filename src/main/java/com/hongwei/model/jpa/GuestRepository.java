package com.hongwei.model.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    @Query("from Guest g where g.guest_code=:guest_code")
    Guest findByGuestCode(@Param("guest_code") String guest_code);

    @Transactional
    @Modifying
    @Query("delete from Guest g where g.guest_code=:guest_code")
    void deleteByGuestCode(@Param("guest_code") String guest_code);
}