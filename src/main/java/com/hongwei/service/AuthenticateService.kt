package com.hongwei.service

import com.hongwei.constants.BadRequest
import com.hongwei.constants.Constants.Guest.GUEST_PASS
import com.hongwei.constants.Constants.TimeSpan.DAY
import com.hongwei.constants.Constants.TimeSpan.MINUTE
import com.hongwei.constants.SecurityConfigurations
import com.hongwei.constants.Unauthorized
import com.hongwei.model.contract.Role
import com.hongwei.model.jpa.GuestRepository
import com.hongwei.model.jpa.UserRepository
import com.hongwei.security.JwtUtil
import com.hongwei.security.model.*
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
class AuthenticateService {
    private val logger: Logger = LogManager.getLogger(AuthenticateService::class.java)

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var guestRepository: GuestRepository

    @Autowired
    private lateinit var securityConfigurations: SecurityConfigurations

    fun authorise(authorisationRequest: AuthorisationRequest): AuthorisationResponse {
        val username = jwtUtil.extractUsername(authorisationRequest.token)
        val userDetails = userDetailsService.loadUserByUsername(username)
        if (jwtUtil.validateToken(authorisationRequest.token, userDetails)) {
            val role: String
            val preferenceJson: String
            if (isGuest(username)) {
                role = Role.guest.toString()
                val guest = guestRepository.findByGuestCode(username)
                preferenceJson = guest.preference_json
            } else {
                val user = userRepository.findByUserName(username)
                role = user.role
                preferenceJson = user.preference_json
            }
            return AuthorisationResponse(
                    validated = true,
                    userName = username,
                    role = role,
                    preferenceJson = preferenceJson
            )
        }
        throw Unauthorized
    }

    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse {
        val username = jwtUtil.extractUsername(refreshTokenRequest.refreshToken)
        val userDetails = userDetailsService.loadUserByUsername(username)
        if (jwtUtil.validateToken(refreshTokenRequest.refreshToken, userDetails)) {
            return RefreshTokenResponse(
                    jwtUtil.generateToken(userDetails, securityConfigurations.tokenExpirationMin * MINUTE)
            )
        }
        throw Unauthorized
    }

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
            (securityConfigurations.refreshTokenExpirationGuestDay * DAY).coerceAtMost(left)
        } else {
            securityConfigurations.refreshTokenExpirationUserDay * DAY
        }

        val userDetails: UserDetails = userDetailsService.loadUserByUsername(user)
        val refreshToken = jwtUtil.generateToken(userDetails, expiration)
        val token = jwtUtil.generateToken(userDetails, securityConfigurations.tokenExpirationMin * MINUTE)
        return AuthenticationResponse(token, refreshToken)
    }
}