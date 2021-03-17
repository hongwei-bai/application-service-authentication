package com.hongwei.service

import com.hongwei.constants.BadRequest
import com.hongwei.constants.Constants.Guest.GUEST_PASS
import com.hongwei.constants.Constants.Security.GUEST_TOKEN_EXPIRATION
import com.hongwei.constants.Constants.Security.USER_TOKEN_EXPIRATION
import com.hongwei.constants.Unauthorized
import com.hongwei.model.jpa.GuestRepository
import com.hongwei.security.JwtUtil
import com.hongwei.security.model.AuthenticationRequest
import com.hongwei.security.model.AuthenticationResponse
import com.hongwei.util.isGuest
import org.apache.log4j.LogManager
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class LoginService {
    private val logger: Logger = LogManager.getLogger(LoginService::class.java)

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Autowired
    private lateinit var guestRepository: GuestRepository

    fun authenticate(authenticationRequest: AuthenticationRequest): AuthenticationResponse =
            with(authenticationRequest) {
                if (userName != null && credential != null) {
                    authenticateImplement(userName, credential)
                } else if (guestCode != null) {
                    authenticateImplement("$guestCode", GUEST_PASS)
                } else {
                    throw BadRequest
                }
            }

    private fun authenticateImplement(user: String, passwordHash: String): AuthenticationResponse {
        try {
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            user,
                            passwordHash.toLowerCase()
                    )
            )
        } catch (e: BadCredentialsException) {
            throw Unauthorized
        }

        val expiration = if (isGuest(user)) {
            val guest = guestRepository.findByGuestCode(user)
            val left = guest.expire_time - System.currentTimeMillis()
            logger.debug("left: $left")
            if (left < 0) {
                throw Unauthorized
            }
            GUEST_TOKEN_EXPIRATION.coerceAtMost(left)
        } else {
            USER_TOKEN_EXPIRATION
        }

        val userDetails: UserDetails = userDetailsService.loadUserByUsername(user)
        val jwt = jwtUtil.generateToken(userDetails, expiration)
        return AuthenticationResponse(jwt)
    }
}