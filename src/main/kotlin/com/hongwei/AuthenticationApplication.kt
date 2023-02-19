package com.hongwei

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

//@EntityScan
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
//@EnableScheduling
//@SpringBootApplication
class AuthenticationApplication

fun main(args: Array<String>) {
    runApplication<AuthenticationApplication>(*args)
}
