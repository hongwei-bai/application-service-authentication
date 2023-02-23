package com.hongwei.security

import com.hongwei.constants.Constants.TimeSpan.DAY
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import java.util.*

@Service
class PublicTokenService {
    @Autowired
    private lateinit var jwtUtil: JwtUtil

    fun generateToken(userName: String, expirationDay: Long, secret: String): String =
            jwtUtil.generateToken(User(userName, "", emptyList()), expirationDay * DAY, secret)

    fun extractUserName(token: String, secret: String): String = jwtUtil.extractUsername(token, secret)

    fun extractExpiration(token: String, secret: String): Date = jwtUtil.extractExpiration(token, secret)

    fun validateToken(token: String, userName: String, secret: String): Boolean =
            jwtUtil.validateToken(token, User(userName, "", emptyList()), secret)
}