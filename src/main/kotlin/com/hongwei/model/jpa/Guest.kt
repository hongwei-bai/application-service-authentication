package com.hongwei.model.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Lob
import org.springframework.data.annotation.Id

@Entity
data class Guest(
        @jakarta.persistence.Id @Id
        @GeneratedValue
        var id: Long? = null,

        @Column(nullable = false)
        var expireTime: Long = System.currentTimeMillis(),

        @Column(nullable = false)
        var guestCode: String = "",

        @Lob
        @Column(nullable = false)
        var preferenceJson: String = "{}",

        @Lob
        @Column(nullable = false)
        var privilegeJson: String = "{}"
)