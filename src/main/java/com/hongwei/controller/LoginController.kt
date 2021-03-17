package com.hongwei.controller

import com.hongwei.constants.Constants.Security.AUTHENTICATE_PATH
import com.hongwei.security.model.AuthenticationRequest
import com.hongwei.service.LoginService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class LoginController {
    @Autowired
    private lateinit var loginService: LoginService

    @RequestMapping(value = [AUTHENTICATE_PATH], method = [RequestMethod.POST])
    @ResponseBody
    fun login(@RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<*> =
            ResponseEntity.ok(loginService.authenticate(authenticationRequest))
}