package com.hongwei.model.jpa

import javax.persistence.*

@Entity
data class Guest(
        @Id
        @GeneratedValue
        var id: Long? = null,

        @Column(nullable = false)
        var expireTime: Long = System.currentTimeMillis(),

        @Column(nullable = false)
        var description: String = "",

        @Column(nullable = false)
        var guestCode: String = "",

        @Lob
        @Column(nullable = false)
        var preferenceJson: String = "{}",

        @Lob
        @Column(nullable = false)
        var privilegeJson: String = "{}"
)