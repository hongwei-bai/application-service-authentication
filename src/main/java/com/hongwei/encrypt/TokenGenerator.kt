package com.hongwei.encrypt

import com.hongwei.encrypt.util.SaltUtils
import java.util.*

object TokenGenerator {
    @JvmStatic
    fun generateToken(username: String, saltLength: Int): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append(username)
        stringBuilder.append(System.currentTimeMillis())
        stringBuilder.append(SaltUtils.getSalt(saltLength))
        return Base64.getEncoder().withoutPadding().encodeToString(stringBuilder.toString().toByteArray())
    }
}