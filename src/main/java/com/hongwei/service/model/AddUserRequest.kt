package com.hongwei.service.model

data class AddUserRequest(
        val userName: String,
        val role: String,
        val passwordHash: String
)