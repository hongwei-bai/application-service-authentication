package com.hongwei.model.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface GuestRepository : JpaRepository<Guest, Long> {
    @Query("from Guest g where g.guestCode=:guestCode")
    fun findByGuestCode(@Param("guestCode") guestCode: String): Guest?

    @Query("from Guest g")
    fun findAllGuest(): List<Guest>

    @Transactional
    @Modifying
    @Query("delete from Guest g where g.guestCode=:guestCode")
    fun deleteByGuestCode(@Param("guestCode") guestCode: String)
}