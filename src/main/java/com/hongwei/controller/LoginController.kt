package com.hongwei.controller

import com.hongwei.constants.Constants.Security.LOGIN_PATH
import com.hongwei.security.model.AuthenticationRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class LoginController {
    @Autowired
    private lateinit var authenticateController: AuthenticateController

    @RequestMapping(value = [LOGIN_PATH], method = [RequestMethod.POST])
    @ResponseBody
    fun login(@RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<*> =
            authenticateController.authenticate(authenticationRequest)
}