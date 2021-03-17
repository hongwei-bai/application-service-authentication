package com.hongwei.security.model

data class AuthenticationRequest(
        val guestCode: String?,
        val userName: String?,
        val credential: String? // password hash
)