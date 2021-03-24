package com.hongwei.controller

import com.hongwei.security.model.AuthenticationRequest
import com.hongwei.security.model.AuthorisationRequest
import com.hongwei.security.model.RefreshTokenRequest
import com.hongwei.service.AuthenticateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/authenticate")
@CrossOrigin
class AuthenticateController {
    @Autowired
    private lateinit var authenticateService: AuthenticateService

    @RequestMapping(value = ["/login.do"], method = [RequestMethod.POST])
    @ResponseBody
    fun authenticate(@RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<*> =
            ResponseEntity.ok(authenticateService.authenticate(authenticationRequest))

    @RequestMapping(value = ["/refreshToken.do"], method = [RequestMethod.POST])
    @ResponseBody
    fun refreshToken(@RequestBody refreshTokenRequest: RefreshTokenRequest): ResponseEntity<*> =
            ResponseEntity.ok(authenticateService.refreshToken(refreshTokenRequest))

    @RequestMapping(value = ["/authorise.do"], method = [RequestMethod.POST])
    @ResponseBody
    fun authorise(@RequestBody authorisationRequest: AuthorisationRequest): ResponseEntity<*> =
            ResponseEntity.ok(authenticateService.authorise(authorisationRequest))
}