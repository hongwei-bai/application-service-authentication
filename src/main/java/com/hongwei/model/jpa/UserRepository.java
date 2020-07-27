package com.hongwei.model.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("from User u where u.user_name=:user_name")
    User findByUserName(@Param("user_name") String user_name);

    @Query("from User u where u.token=:token")
    User findByUserToken(@Param("token") String token);

    @Transactional
    @Modifying
    @Query("delete from User u where u.user_name=:user_name")
    void deleteByUserName(@Param("user_name") String user_name);
}