package com.hongwei.security.model

data class AuthenticationResponse(
        val accessToken: String,
        val refreshToken: String,
        val role: String,
        val preferenceJson: String,
        val privilegeJson: String
)