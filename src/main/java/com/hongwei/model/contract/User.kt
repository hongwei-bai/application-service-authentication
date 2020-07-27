package com.hongwei.model.contract

data class User(
        val username: String,
        val role: Roles,
        val access: List<Access>
)