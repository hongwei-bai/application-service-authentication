package com.hongwei.security.filters

import com.hongwei.constants.Constants.Security.PRE_FLIGHT_STUB_USER
import com.hongwei.constants.SecurityConfigurations
import com.hongwei.security.AccessTokenService
import com.hongwei.service.AuthenticateUserDetailsService
import org.apache.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders.*
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
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
    private val _logger = LogManager.getLogger(JwtRequestFilter::class.java)

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

        /*
        The W3 spec for CORS preflight requests clearly states that user credentials should be excluded.
        Reference[a]: https://stackoverflow.com/questions/15734031/why-does-the-preflight-options-request-of-an-authenticated-cors-request-work-in
        Reference[b]: https://fetch.spec.whatwg.org/#cors-protocol-and-credentials
         */
        if (request.method == HttpMethod.OPTIONS.name) {
            grantAccess(request)
        } else if (authorizationHeader != null && authorizationHeader.startsWith(securityConfigurations.authorizationBearer)) {
            jwt = authorizationHeader.substring(securityConfigurations.authorizationBearer.length + 1)
            username = accessTokenService.extractUsername(jwt)

            if (SecurityContextHolder.getContext().authentication == null) {
                grantAccess(request, username)
            }
        }

        appendCORSHeaders(response)
        chain.doFilter(request, response)
    }

    private fun grantAccess(request: HttpServletRequest, userName: String = PRE_FLIGHT_STUB_USER) {
        val userDetails = userDetailsService.loadUserByUsername(userName)

        // Grant access
        val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                userDetails, userDetails.password, userDetails.authorities)
        usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
    }

    private fun appendCORSHeaders(response: HttpServletResponse) {
        response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, true.toString())
        response.addHeader(ACCESS_CONTROL_ALLOW_HEADERS, "$CONTENT_TYPE,${securityConfigurations.authorizationHeader}")
        response.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, securityConfigurations.corsAllowDomain)
        response.addHeader(CONTENT_TYPE,"${MediaType.APPLICATION_JSON.type},${MediaType.MULTIPART_FORM_DATA.type}")
    }
}