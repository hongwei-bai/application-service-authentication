package com.hongwei.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function

@Service
class JwtUtil {
    companion object {
        private val SECRET_KEY = "secret"

        private val SECOND = 1000L
        private val MINUTE = 60000
        private val HOUR = 3600000
        private val DAY = HOUR * 24
    }

    fun extractUsername(token: String?): String {
        return extractClaim(token, Function { obj: Claims -> obj.subject })
    }

    fun extractExpiration(token: String?): Date {
        return extractClaim(token, Function { obj: Claims -> obj.expiration })
    }

    fun <T> extractClaim(token: String?, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    private fun extractAllClaims(token: String?): Claims {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).body
    }

    private fun isTokenExpired(token: String?): Boolean? {
        return extractExpiration(token).before(Date())
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims: Map<String, Any> = HashMap()
        return createToken(claims, userDetails.username)
    }

    private fun createToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + DAY * 60L))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact()
    }

    fun validateToken(token: String?, userDetails: UserDetails): Boolean? {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)!!
    }
}