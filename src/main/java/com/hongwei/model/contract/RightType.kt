package com.hongwei.model.contract

data class RightType(
        var read: Boolean,
        var update: Boolean,
        var admin: Boolean
)