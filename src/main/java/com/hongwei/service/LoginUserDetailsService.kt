package com.hongwei.service

import com.hongwei.constants.Constants.Guest.GUEST_PASS
import com.hongwei.constants.Constants.Guest.GUEST_PREFIX
import com.hongwei.constants.Unauthorized
import com.hongwei.model.jpa.GuestRepository
import com.hongwei.model.jpa.UserRepository
import org.apache.log4j.LogManager
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class LoginUserDetailsService : UserDetailsService {
    private val logger: Logger = LogManager.getLogger(LoginUserDetailsService::class.java)

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var guestRepository: GuestRepository

    override fun loadUserByUsername(userName: String): UserDetails =
            if (userName.startsWith(GUEST_PREFIX)) {
                loadGuest(userName)
            } else {
                bypassTmp(userName)
            }

    //TODO to remove
    private fun bypassTmp(userName: String): UserDetails {
        if (userName == "baihongwei") {
            return User("baihongwei", "666", emptyList())
        } else {
            return loadUser(userName)
        }
    }

    private fun loadUser(userName: String): UserDetails =
            userRepository.findByUserName(userName)?.run {
                User(user_name, password_hash, emptyList())
            } ?: throw Unauthorized

    private fun loadGuest(user: String): UserDetails =
            guestRepository.findByGuestCode(user)?.run {
                User(user, GUEST_PASS, emptyList())
            } ?: throw Unauthorized
}
