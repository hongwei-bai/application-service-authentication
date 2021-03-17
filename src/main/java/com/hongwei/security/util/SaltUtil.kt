package com.hongwei.security.util

import com.hongwei.constants.Constants.Security.SALT_CHAR_POOL
import java.util.*

object SaltUtils {
    @JvmStatic
    fun getSalt(n: Int): String {
        val chars = (SALT_CHAR_POOL).toCharArray()
        val sb = StringBuilder()
        for (i in 0 until n) {
            val aChar = chars[Random().nextInt(chars.size)]
            sb.append(aChar)
        }
        return sb.toString()
    }
}