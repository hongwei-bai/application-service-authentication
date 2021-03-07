package com.hongwei.controller

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hongwei.controller.model.ErrorCode
import com.hongwei.controller.model.Response
import com.hongwei.service.RegisterService
import com.hongwei.service.model.Failure
import com.hongwei.service.model.Success
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/register")
@CrossOrigin
class RegisterController {
    @Autowired
    private lateinit var registerService: RegisterService

    @PutMapping(path = ["/user.do"])
    @ResponseBody
    fun registerUser(userName: String, passwordHash: String): String =
            when (val result = registerService.registerUser(userName, passwordHash)) {
                is Success<*> -> Gson().toJson(Response<Any>().apply {
                    code = ErrorCode.CODE_SUCCESS
                    msg = "Register user success"
                }, object : TypeToken<Response<*>>() {}.type)
                is Failure -> Gson().toJson(Response.from(ErrorCode.CODE_ERROR, result.message ?: ""))
            }
}