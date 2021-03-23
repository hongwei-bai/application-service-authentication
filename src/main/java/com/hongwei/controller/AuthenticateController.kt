package com.hongwei.controller

import com.hongwei.constants.Constants.Security.AUTHENTICATE_PATH
import com.hongwei.constants.Constants.Security.REFRESH_TOKEN_PATH
import com.hongwei.security.model.AuthenticationRequest
import com.hongwei.security.model.AuthorisationRequest
import com.hongwei.security.model.RefreshTokenRequest
import com.hongwei.service.AuthenticateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class AuthenticateController {
    @Autowired
    private lateinit var authenticateService: AuthenticateService

    @RequestMapping(value = [AUTHENTICATE_PATH], method = [RequestMethod.POST])
    @ResponseBody
    fun authenticate(@RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<*> =
            ResponseEntity.ok(authenticateService.authenticate(authenticationRequest))

    @RequestMapping(value = [REFRESH_TOKEN_PATH], method = [RequestMethod.POST])
    @ResponseBody
    fun refreshToken(@RequestBody refreshTokenRequest: RefreshTokenRequest): ResponseEntity<*> =
            ResponseEntity.ok(authenticateService.refreshToken(refreshTokenRequest))

    @RequestMapping(value = ["/authorise.do"], method = [RequestMethod.POST])
    @ResponseBody
    fun authorization(@RequestBody authorisationRequest: AuthorisationRequest): ResponseEntity<*> =
            ResponseEntity.ok(authenticateService.authorise(authorisationRequest))
}