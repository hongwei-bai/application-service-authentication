package com.hongwei.security.model

data class AuthenticationResponse(
        val token: String,
        val refreshToken: String
)