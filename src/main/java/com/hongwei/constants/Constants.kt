package com.hongwei.constants

object Constants {
    object Security {
        const val SALT_CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*()_+"

        const val LOGIN_PATH = "/login.do"
        const val AUTHENTICATE_PATH = "/authenticate.do"
        const val REFRESH_TOKEN_PATH = "/refresh_token.do"
        const val INDEX_PATH = "/index.do"
        const val INDEX_ALIAS = "/"
    }

    object Guest {
        val GUEST_CODE_TABLE = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
        const val GUEST_PREFIX = "g:"
        const val GUEST_PASS = "guest_pass"
    }

    object TimeSpan {
        internal const val SECOND = 1000L
        internal const val MINUTE = 60000L
        internal const val HOUR = 3600000L
        internal const val DAY = HOUR * 24L
    }
}