package com.hongwei.security.model

data class AuthenticationRequest(
        val username: String,
        val password: String
)