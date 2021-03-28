package com.hongwei.model.privilege

data class AdminPrivilege(
        val addUser: Boolean = false,
        val modUser: Boolean = false,
        val delUser: Boolean = false,
        val addGuest: Boolean = false,
        val modGuest: Boolean = false,
        val delGuest: Boolean = false
)