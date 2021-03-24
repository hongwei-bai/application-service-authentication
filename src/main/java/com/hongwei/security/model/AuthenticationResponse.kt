package com.hongwei.security.model

data class AuthenticationResponse(
        val accessToken: String,
        val refreshToken: String
)