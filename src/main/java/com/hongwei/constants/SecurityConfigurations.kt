package com.hongwei.constants

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import kotlin.properties.Delegates

@Configuration
@ConfigurationProperties(prefix = "jwt")
open class SecurityConfigurations {
    lateinit var secret: String
    lateinit var secretRefresh: String
    lateinit var authorizationHeader: String
    lateinit var authorizationBearer: String
    lateinit var corsAllowDomain: String
    var tokenExpirationMin by Delegates.notNull<Long>()
    var refreshTokenExpirationUserDay by Delegates.notNull<Long>()
    var refreshTokenExpirationGuestDay by Delegates.notNull<Long>()
    var guestCodeLength by Delegates.notNull<Int>()
}