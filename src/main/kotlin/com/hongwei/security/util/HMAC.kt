package com.hongwei.security.util

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HMAC {
    @JvmStatic
    fun calcHmacSha256(secretKey: ByteArray?, message: ByteArray?): ByteArray {
        val hmacSha256: ByteArray = try {
            val mac = Mac.getInstance("HmacSHA256")
            val secretKeySpec = SecretKeySpec(secretKey, "HmacSHA256")
            mac.init(secretKeySpec)
            mac.doFinal(message)
        } catch (e: Exception) {
            throw RuntimeException("Failed to calculate hmac-sha256", e)
        }
        return hmacSha256
    }
}