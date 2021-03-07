package com.hongwei.controller

import com.hongwei.security.JwtUtil
import com.hongwei.security.model.AuthenticationRequest
import com.hongwei.security.model.AuthenticationResponse
import org.apache.log4j.LogManager
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class IndexController {
    private val logger: Logger = LogManager.getLogger(IndexController::class.java)

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Suppress("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @RequestMapping(path = ["/index.do", "/"])
    @ResponseBody
    fun index(): String {
        logger.debug("Hello debug")
        val stringBuilder = StringBuilder()
        return "Hongwei Hello My SpringBoot!$stringBuilder"
    }

    @RequestMapping(value = ["/authenticate"], method = [RequestMethod.POST])
    @ResponseBody
    fun createAuthenticationToken(@RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<*> {
        try {
            authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            authenticationRequest.username,
                            authenticationRequest.password
                    )
            )
        } catch (e: BadCredentialsException) {
            throw Exception("Incorrect username or password", e)
        }

        val userDetails: UserDetails = userDetailsService.loadUserByUsername(authenticationRequest.username)
        val jwt = jwtUtil.generateToken(userDetails)

        return ResponseEntity.ok(AuthenticationResponse(jwt))
    }
}