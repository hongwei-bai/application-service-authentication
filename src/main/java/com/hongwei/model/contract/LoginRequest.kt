package com.hongwei.model.contract

data class LoginRequest(
        val userName: String,
        val passwordHash: String
)