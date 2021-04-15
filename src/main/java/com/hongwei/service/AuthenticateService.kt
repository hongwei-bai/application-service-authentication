package com.hongwei.service

import com.fasterxml.jackson.core.JsonParseException
import com.hongwei.constants.*
import com.hongwei.constants.Constants.Guest.GUEST_PASS
import com.hongwei.constants.Constants.TimeSpan.DAY
import com.hongwei.constants.Constants.TimeSpan.MINUTE
import com.hongwei.model.contract.Role
import com.hongwei.model.jpa.GuestRepository
import com.hongwei.model.jpa.UserRepository
import com.hongwei.security.AccessTokenService
import com.hongwei.security.RefreshTokenService
import com.hongwei.security.model.*
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import org.apache.log4j.LogManager
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class AuthenticateService {
    private val logger: Logger = LogManager.getLogger(AuthenticateService::class.java)

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var accessTokenService: AccessTokenService

    @Autowired
    private lateinit var refreshTokenService: RefreshTokenService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var guestRepository: GuestRepository

    @Autowired
    private lateinit var securityConfigurations: SecurityConfigurations

    fun authorise(authorisationRequest: AuthorisationRequest): AuthorisationResponse {
        try {
            val username = accessTokenService.extractUsername(authorisationRequest.accessToken)
            val userDetails = userDetailsService.loadUserByUsername(username)
            if (accessTokenService.validateToken(authorisationRequest.accessToken, userDetails)) {
                var role = ""
                var preferenceJson = ""
                var privilegeJson = ""
                var validatedUntil = LONG_TERM
                if (isGuest(username)) {
                    role = Role.guest.toString()
                    guestRepository.findByGuestCode(username)?.let { guest ->
                        preferenceJson = guest.preferenceJson
                        privilegeJson = guest.privilegeJson
                        validatedUntil = guest.expireTime
                    } ?: throw NotFound
                } else {
                    userRepository.findByUserName(username)?.let { user ->
                        role = user.role
                        preferenceJson = user.preferenceJson
                        privilegeJson = user.privilegeJson
                    } ?: throw NotFound
                }
                return AuthorisationResponse(
                        validated = true,
                        validatedUntil = validatedUntil,
                        userName = username,
                        role = role,
                        preferenceJson = preferenceJson,
                        privilegeJson = privilegeJson
                )
            }
        } catch (e: ExpiredJwtException) {
            throw e
        } catch (e: MalformedJwtException) {
            throw Unauthorized
        }
        throw Unauthorized
    }

    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse {
        val username = refreshTokenService.extractUsername(refreshTokenRequest.refreshToken)
        val userDetails = userDetailsService.loadUserByUsername(username)
        if (refreshTokenService.validateToken(refreshTokenRequest.refreshToken, userDetails)) {
            return RefreshTokenResponse(
                    accessTokenService.generateToken(userDetails, securityConfigurations.tokenExpirationMin * MINUTE)
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
            val guest = guestRepository.findByGuestCode(user) ?: throw NotFound
            val left = guest.expireTime - System.currentTimeMillis()
            logger.debug("left: $left")
            if (left < 0) {
                throw Unauthorized
            }
            (securityConfigurations.refreshTokenExpirationGuestDay * DAY).coerceAtMost(left)
        } else {
            securityConfigurations.refreshTokenExpirationUserDay * DAY
        }

        val userDetails: UserDetails = userDetailsService.loadUserByUsername(user)
        val refreshToken = refreshTokenService.generateToken(userDetails, expiration)
        val token = accessTokenService.generateToken(userDetails, securityConfigurations.tokenExpirationMin * MINUTE)
        var role = ""
        var preferenceJson = ""
        var privilegeJson = ""
        if (isGuest(user)) {
            guestRepository.findByGuestCode(user)?.let {
                role = Role.guest.name
                preferenceJson = it.preferenceJson
                privilegeJson = it.privilegeJson
            } ?: throw NotFound
        } else {
            userRepository.findByUserName(user)?.let {
                role = it.role
                preferenceJson = it.preferenceJson
                privilegeJson = it.privilegeJson
            } ?: throw NotFound
        }
        return AuthenticationResponse(token, refreshToken, role, preferenceJson, privilegeJson)
    }

    private fun isGuest(user: String) = user.startsWith(Constants.Guest.GUEST_PREFIX, false)
}