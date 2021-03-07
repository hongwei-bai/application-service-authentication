package com.hongwei.controller

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hongwei.controller.model.ErrorCode
import com.hongwei.controller.model.Response
import com.hongwei.service.LoginService
import com.hongwei.service.model.Failure
import com.hongwei.service.model.LoginResponseBody
import com.hongwei.service.model.Success
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/login")
@CrossOrigin
class LoginController {
    @Autowired
    private lateinit var loginService: LoginService

    @RequestMapping(path = ["/user.do"])
    @ResponseBody
    fun login(@RequestParam userName: String, @RequestParam passwordHash: String): String =
            when (val result = loginService.login(userName, passwordHash)) {
                is Success<*> -> Gson().toJson(Response<LoginResponseBody?>().apply {
                    code = ErrorCode.CODE_SUCCESS
                    msg = "User login success"
                    data = result.body as LoginResponseBody?
                }, object : TypeToken<Response<LoginResponseBody?>>() {}.type)
                is Failure -> Gson().toJson(Response.from(ErrorCode.CODE_LOGIN_FAILURE, result.message ?: ""))
            }

    @RequestMapping(path = ["/guest.do"])
    @ResponseBody
    fun guestLogin(guestCode: String): String =
            when (val result = loginService.guestLogin(guestCode)) {
                is Success<*> -> Gson().toJson(Response<LoginResponseBody?>().apply {
                    code = ErrorCode.CODE_SUCCESS
                    msg = "Guest login success"
                    data = result.body as LoginResponseBody?
                }, object : TypeToken<Response<LoginResponseBody?>>() {}.type)
                is Failure -> Gson().toJson(Response.from(ErrorCode.CODE_LOGIN_FAILURE, result.message ?: ""))
            }
}