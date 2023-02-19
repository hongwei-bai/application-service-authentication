package com.hongwei.service

import com.hongwei.constants.Constants.Guest.GUEST_PASS
import com.hongwei.constants.Constants.Guest.GUEST_PREFIX
import com.hongwei.constants.Constants.Security.PRE_FLIGHT_STUB_USER
import com.hongwei.constants.Unauthorized
import com.hongwei.model.jpa.GuestRepository
import com.hongwei.model.jpa.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticateUserDetailsService : UserDetailsService {
    private val logger: Logger = LoggerFactory.getLogger(AuthenticateUserDetailsService::class.java)

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var guestRepository: GuestRepository

    override fun loadUserByUsername(userName: String): UserDetails =
            when {
                userName == PRE_FLIGHT_STUB_USER -> {
                    User(PRE_FLIGHT_STUB_USER, "", emptyList())
                }
                userName.startsWith(GUEST_PREFIX) -> {
                    loadGuest(userName)
                }
                else -> {
                    loadUser(userName)
                }
            }

    private fun loadUser(userName: String): UserDetails =
            userRepository.findByUserName(userName)?.run {
                User(userName, credential.lowercase(Locale.getDefault()), emptyList())
            } ?: throw Unauthorized

    private fun loadGuest(user: String): UserDetails =
            guestRepository.findByGuestCode(user)?.run {
                User(user, GUEST_PASS, emptyList())
            } ?: throw Unauthorized
}
