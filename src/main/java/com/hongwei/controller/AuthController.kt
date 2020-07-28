package com.hongwei.controller

import com.google.gson.Gson
import com.hongwei.Constant
import com.hongwei.model.contract.LoginRequest
import com.hongwei.model.jpa.GuestRepository
import com.hongwei.model.jpa.User
import com.hongwei.model.jpa.UserRepository
import com.hongwei.model.soap.common.Response
import com.hongwei.model.soap.common.SoapConstant.AUTH_FAILURE
import com.hongwei.model.soap.common.SoapConstant.CODE_ERROR
import com.hongwei.model.soap.common.SoapConstant.CODE_LOGIN_FAILURE
import com.hongwei.model.soap.common.SoapConstant.CODE_SUCCESS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/auth")
@CrossOrigin
class AuthController {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var guestRepository: GuestRepository

    private val tmpToken: String = getTmpToken()

    @RequestMapping(path = ["/login.do"])
    @ResponseBody
    fun login(@RequestParam userName: String, @RequestParam passwordHash: String, @RequestParam sign: String?): String {
        val user = userRepository.findByUserName(userName)
                ?: return Gson().toJson(Response.from(CODE_LOGIN_FAILURE, "User not exist."))

        if (passwordHash != user.password_hash) {
            return Gson().toJson(Response.from(CODE_LOGIN_FAILURE, "Login failed!"))
        }

        return Gson().toJson(Response.from(CODE_SUCCESS, "Login success", user))
    }

    @RequestMapping(path = ["/guestLogin.do"])
    @ResponseBody
    fun guestLogin(guestCode: String, sign: String?): String {
        val guest = guestRepository.findByGuestCode(guestCode)
                ?: return Gson().toJson(Response.from(AUTH_FAILURE, "invalid guest code."))

        if (guest.expire_time < System.currentTimeMillis()) {
            return Gson().toJson(Response.from(AUTH_FAILURE, "invalid guest code: expired!"))
        }

        return Gson().toJson(Response.from(CODE_SUCCESS, "guest login success", guest))
    }

    @GetMapping(path = ["/checkUserNameExist.do"])
    @ResponseBody
    fun checkUserNameExist(userName: String, sign: String?): String {
        val exist: Boolean = userRepository.findByUserName(userName) != null
        return Gson().toJson(Response.from(CODE_SUCCESS, "query success", exist))
    }

    @PutMapping(path = ["/register.do"])
    @ResponseBody
    fun register(userName: String, passwordHash: String, sign: String?): String {
        userRepository.findByUserName(userName)?.let {
            return Gson().toJson(Response.from(CODE_ERROR, "username already exist"))
        }
        val user = User().apply {
            user_name = userName
            password_hash = passwordHash
            this.roles = roles
            token = tmpToken
        }
        userRepository.save(user)
        return Gson().toJson(Response.from(CODE_SUCCESS, "register success", user))
    }

    private fun getTmpToken(): String {
        return Constant.ACCESS_TOKEN
    }
}