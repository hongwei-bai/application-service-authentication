package com.hongwei.service

import com.hongwei.Constant
import com.hongwei.model.jpa.GuestRepository
import com.hongwei.model.jpa.UserRepository
import com.hongwei.service.model.Failure
import com.hongwei.service.model.LoginResponseBody
import com.hongwei.service.model.ServiceResult
import com.hongwei.service.model.Success
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LoginService {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var guestRepository: GuestRepository

    @Autowired
    private lateinit var tokenService: TokenService

    private val timeStampByUserName = hashMapOf<String, Long>()
    private val counterByUserName = hashMapOf<String, Int>()

    fun login(userName: String, passwordHash: String): ServiceResult {
        if (!check(userName)) {
            return Failure("Brutal attempts")
        }

        val user = userRepository.findByUserName(userName)
                ?: return Failure("User not exist")

        if (passwordHash != user.password_hash) {
            return Failure("Login failed!")
        }

        val token = tokenService.generateToken(userName)
        val refreshToken = tokenService.generateRefreshToken(userName)
        tokenService.saveToken(userName, token, refreshToken)
        return Success(LoginResponseBody(token, refreshToken))
    }

    fun guestLogin(guestCode: String): ServiceResult {
        if (!check(guestCode)) {
            return Failure("Brutal attempts")
        }

        val guest = guestRepository.findByGuestCode(guestCode)
                ?: return Failure("Invalid guest code")

        if (guest.expire_time < System.currentTimeMillis()) {
            return Failure("Guest code: expired")
        }

        val token = tokenService.generateToken(guestCode)
        val refreshToken = tokenService.generateRefreshToken(guestCode)
        tokenService.saveToken(guestCode, token, refreshToken)
        return Success(LoginResponseBody(token, refreshToken))
    }

    private fun check(userName: String): Boolean {
        if (!timeStampByUserName.containsKey(userName)) {
            timeStampByUserName[userName] = System.currentTimeMillis()
            counterByUserName[userName] = 0
        }

        val lastTimeStamp = timeStampByUserName[userName] ?: 0L
        val count = counterByUserName[userName] ?: 0

        if (count >= Constant.LoginSecurity.MAX_COUNT) {
            if (System.currentTimeMillis() - lastTimeStamp > Constant.LoginSecurity.MAX_REATTEMPT_DURATION) {
                timeStampByUserName[userName] = System.currentTimeMillis()
                counterByUserName[userName] = 0
                return true
            } else {
                return false
            }
        } else {
            counterByUserName[userName] = count + 1
            return true
        }
    }
}