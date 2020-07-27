package com.hongwei.model.contract

data class AddUserRequest(
        val userName: String,
        val roles: String,
        val passwordHash: String
)