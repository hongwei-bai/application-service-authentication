package com.hongwei.security.filters

import com.hongwei.constants.SecurityConfigurations
import com.hongwei.security.AccessTokenService
import com.hongwei.service.AuthenticateUserDetailsService
import org.apache.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter : OncePerRequestFilter() {
    private val logger = LogManager.getLogger(JwtRequestFilter::class.java)

    @Autowired
    private lateinit var userDetailsService: AuthenticateUserDetailsService

    @Autowired
    private lateinit var accessTokenService: AccessTokenService

    @Autowired
    private lateinit var securityConfigurations: SecurityConfigurations

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authorizationHeader = request.getHeader(securityConfigurations.authorizationHeader)
        val username: String
        val jwt: String

        if (authorizationHeader != null && authorizationHeader.startsWith(securityConfigurations.authorizationBearer)) {
            jwt = authorizationHeader.substring(securityConfigurations.authorizationBearer.length + 1)
            username = accessTokenService.extractUsername(jwt)

            if (SecurityContextHolder.getContext().authentication == null) {
                val userDetails = userDetailsService.loadUserByUsername(username)
                if (accessTokenService.validateToken(jwt, userDetails)) {
                    // Grant access
                    val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.authorities)
                    usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
                }
            }
        }

        chain.doFilter(request, response)
    }
}