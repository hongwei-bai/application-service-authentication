package com.hongwei.controller

import com.hongwei.service.RegisterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/register")
@CrossOrigin
class RegisterController {
    @Autowired
    private lateinit var registerService: RegisterService

    @PutMapping(path = ["/user.do"])
    @ResponseBody
    fun registerUser(userName: String, passwordHash: String): ResponseEntity<*> =
            ResponseEntity.ok(registerService.registerUser(userName, passwordHash))
}