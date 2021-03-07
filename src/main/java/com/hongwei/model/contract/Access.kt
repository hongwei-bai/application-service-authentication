package com.hongwei.model.contract

data class Access(
        val accessName: String,
        val accessLevel: AccessLevel = AccessLevel()
)