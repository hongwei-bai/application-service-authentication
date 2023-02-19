package com.hongwei.model.jpa

import com.hongwei.model.contract.Role
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Lob
import org.springframework.data.annotation.Id

@Entity
data class User(
        @jakarta.persistence.Id @Id
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