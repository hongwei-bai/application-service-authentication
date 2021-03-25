package com.hongwei.model.jpa

import com.hongwei.model.contract.Role
import javax.persistence.*

@Entity
data class User(
        @Id
        @GeneratedValue
        var id: Long? = null,

        @Column(nullable = false)
        var userName: String = "",

        @Column(nullable = false)
        var credential: String = "",

        @Column(nullable = false)
        var role: String = Role.user.name,

        @Lob
        @Column(nullable = false)
        var preferenceJson: String = "{}",

        @Lob
        @Column(nullable = false)
        var privilegeJson: String = "{}"
)