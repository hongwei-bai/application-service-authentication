package com.hongwei.controller.test

import com.hongwei.constants.Constants.Security.INDEX_ALIAS
import com.hongwei.constants.Constants.Security.INDEX_PATH
import org.apache.log4j.LogManager
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class TestController {
    private val logger: Logger = LogManager.getLogger(TestController::class.java)

    @Value("\${spring.jmx.default-domain}")
    private lateinit var applicationDomain: String

    @RequestMapping(path = [INDEX_PATH, INDEX_ALIAS])
    @ResponseBody
    fun index(): String {
        logger.debug("Hello debug")

        return "Hello Hongwei! Welcome to $applicationDomain"
    }
}