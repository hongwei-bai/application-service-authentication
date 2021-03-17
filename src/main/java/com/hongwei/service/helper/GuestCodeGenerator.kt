package com.hongwei.service.helper

import com.hongwei.constants.Constants.Guest.GUEST_CODE_DEFAULT_LENGTH
import com.hongwei.constants.Constants.Guest.GUEST_CODE_TABLE
import java.lang.StringBuilder
import kotlin.random.Random

object GuestCodeGenerator {
    @JvmStatic
    fun generate(length: Int = GUEST_CODE_DEFAULT_LENGTH): String {
        var seed = (length + System.currentTimeMillis()) % 1000
        val stringBuilder = StringBuilder()
        for (i in 0 until length) {
            val randomValue = Random(seed).nextInt(0, GUEST_CODE_TABLE.size)
            stringBuilder.append(GUEST_CODE_TABLE[randomValue])
            seed = (randomValue + length + System.currentTimeMillis()) % 1000
        }
        return stringBuilder.toString()
    }
}