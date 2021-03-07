package com.hongwei.service

import com.hongwei.Constant.LoginSecurity.SALT_LENGTH_REFRESH_TOKEN
import com.hongwei.Constant.LoginSecurity.SALT_LENGTH_TOKEN
import com.hongwei.encrypt.TokenGenerator
import org.springframework.stereotype.Service

@Service
class TokenService {
    private val tokens = hashMapOf<String, TokensByUser>()

    fun generateToken(userName: String): String = TokenGenerator.generateToken(userName, SALT_LENGTH_TOKEN)

    fun generateRefreshToken(userName: String): String = TokenGenerator.generateToken(userName, SALT_LENGTH_REFRESH_TOKEN)

    fun saveToken(userName: String, token: String, refreshToken: String) {
        if (tokens.containsKey(userName)) {
            tokens.remove(userName)
        }
        tokens[userName] = TokensByUser(
                token = token,
                tokenTimeStamp = System.currentTimeMillis(),
                refreshToken = refreshToken,
                refreshTokenTimeStamp = System.currentTimeMillis()
        )
    }

    data class TokensByUser(
            val token: String,
            val tokenTimeStamp: Long,
            val refreshToken: String,
            val refreshTokenTimeStamp: Long
    )
}