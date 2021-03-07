package com.hongwei.encrypt.util

import java.util.*

object SaltUtils {
    @JvmStatic
    fun getSalt(n: Int): String {
        val chars = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" +
                "1234567890!@#$%^&*()_+").toCharArray()
        val sb = StringBuilder()
        for (i in 0 until n) { //Random().nextInt()返回值为[0,n)
            val aChar = chars[Random().nextInt(chars.size)]
            sb.append(aChar)
        }
        return sb.toString()
    }
}