package com.hongwei.controller

import com.hongwei.security.model.GeneratePublicTokenRequest
import com.hongwei.security.model.VerifyPublicTokenRequest
import com.hongwei.service.PublicAuthenticateService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/authenticate")
@RestController
class PublicAuthenticateController {
    @Autowired
    private lateinit var publicAuthenticateService: PublicAuthenticateService

    @RequestMapping(value = ["/generatePublicToken.do"], method = [RequestMethod.POST])
    @ResponseBody
    fun generatePublicToken(@RequestBody generatePublicTokenRequest: GeneratePublicTokenRequest): ResponseEntity<*> =
            ResponseEntity.ok(publicAuthenticateService.generatePublicToken(generatePublicTokenRequest))

    @RequestMapping(value = ["/verifyPublicToken.do"], method = [RequestMethod.POST])
    @ResponseBody
    fun verifyPublicToken(@RequestBody verifyPublicTokenRequest: VerifyPublicTokenRequest): ResponseEntity<*> =
            ResponseEntity.ok(publicAuthenticateService.verifyPublicToken(verifyPublicTokenRequest))
}