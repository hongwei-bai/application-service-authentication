package com.hongwei.service.model

data class LoginResponseBody(
        val token: String,
        val refreshToken: String
)