package com.hongwei.model.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    @Query("from User u where u.userName=:userName")
    fun findByUserName(@Param("userName") userName: String): User?

    @Query("from User u where u.role=:role")
    fun findUsersByRole(@Param("role") role: String): List<User>

    @Transactional
    @Modifying
    @Query("delete from User u where u.userName=:userName")
    fun deleteByUserName(@Param("userName") userName: String)
}