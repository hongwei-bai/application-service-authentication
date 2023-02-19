package com.hongwei.security

import com.hongwei.constants.SecurityConfigurations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class AccessTokenService {
    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Autowired
    private lateinit var securityConfigurations: SecurityConfigurations

    fun extractUsername(token: String?): String = jwtUtil.extractUsername(token, securityConfigurations.secret)

    fun generateToken(userDetails: UserDetails, expiration: Long): String =
            jwtUtil.generateToken(userDetails, expiration, securityConfigurations.secret)

    fun validateToken(token: String?, userDetails: UserDetails): Boolean =
            jwtUtil.validateToken(token, userDetails, securityConfigurations.secret)
}