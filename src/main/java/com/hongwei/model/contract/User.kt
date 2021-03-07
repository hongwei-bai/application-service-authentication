package com.hongwei.model.contract

data class User(
        val username: String,
        val role: Role,
        val access: List<Access>
)