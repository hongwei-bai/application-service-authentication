package com.hongwei

object Constant {
    object LoginSecurity {
        const val MAX_COUNT = 5
        const val MAX_REATTEMPT_DURATION = 5 * 60 * 1000

        const val SALT_LENGTH_TOKEN = 10
        const val SALT_LENGTH_REFRESH_TOKEN = 50
    }
}