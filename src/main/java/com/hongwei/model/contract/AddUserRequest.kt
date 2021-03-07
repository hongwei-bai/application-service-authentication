package com.hongwei.model.contract

data class AddUserRequest(
        val userName: String,
        val role: String,
        val passwordHash: String
)