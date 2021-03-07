package com.hongwei.service.helper

import java.lang.StringBuilder
import kotlin.random.Random

object GuestCodeGenerator {
    @JvmStatic
    fun generate(length: Int = 8): String {
        var seed = (length + System.currentTimeMillis()) % 1000
        val stringBuilder = StringBuilder()
        for (i in 0 until length) {
            val randomValue = Random(seed).nextInt(0, TABLE.size)
            stringBuilder.append(TABLE[randomValue])
            seed = (randomValue + length + System.currentTimeMillis()) % 1000
        }
        return stringBuilder.toString()
    }

    private val TABLE = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
}