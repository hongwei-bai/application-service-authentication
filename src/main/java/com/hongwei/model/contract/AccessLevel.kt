package com.hongwei.model.contract

data class AccessLevel(
        var read: Boolean = false,
        var write: Boolean = false
)