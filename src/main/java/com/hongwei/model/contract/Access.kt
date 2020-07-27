package com.hongwei.model.contract

data class Access(
        val accessName: String,
        val accessRight: RightType = RightType(false, update = false, admin = false)
)