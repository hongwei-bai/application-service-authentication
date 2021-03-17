package com.hongwei.util

import com.hongwei.constants.Constants.Guest.GUEST_PREFIX

fun isGuest(user: String) = user.startsWith(GUEST_PREFIX, false)
