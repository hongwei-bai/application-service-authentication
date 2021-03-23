package com.hongwei.security

import com.hongwei.constants.Constants.Security.AUTHENTICATE_PATH
import com.hongwei.constants.Constants.Security.INDEX_ALIAS
import com.hongwei.constants.Constants.Security.INDEX_PATH
import com.hongwei.constants.Constants.Security.REFRESH_TOKEN_PATH
import com.hongwei.security.filters.JwtRequestFilter
import com.hongwei.service.AuthenticateUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
open class SecurityConfigurer : WebSecurityConfigurerAdapter() {
    @Autowired
    private lateinit var authenticateUserDetailsService: AuthenticateUserDetailsService

    @Autowired
    private val jwtRequestFilter: JwtRequestFilter? = null

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(authenticateUserDetailsService)
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    open fun passwordEncoder(): PasswordEncoder = NoOpPasswordEncoder.getInstance()

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity.csrf().disable()
                .authorizeRequests().antMatchers(
                        AUTHENTICATE_PATH,
                        REFRESH_TOKEN_PATH,
                        INDEX_PATH, INDEX_ALIAS
                )
                .permitAll().anyRequest().authenticated().and().exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
    }
}