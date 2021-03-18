package com.hongwei.controller.test

import org.apache.log4j.LogManager
import org.apache.log4j.Logger
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class TestController {
    private val logger: Logger = LogManager.getLogger(TestController::class.java)

    @RequestMapping(path = ["/index.do", "/"])
    @ResponseBody
    fun index(): String {
        logger.debug("Hello debug")

        return "Hello Hongwei! Welcome to Spring Boot Application - Authentication!"
    }
}