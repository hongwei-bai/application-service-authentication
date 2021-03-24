package com.hongwei.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function
import kotlin.collections.HashMap
import kotlin.collections.Map

@Service
class JwtUtil {
    fun extractUsername(token: String?, secret: String): String {
        return extractClaim(token, secret, Function { obj: Claims -> obj.subject })
    }

    private fun extractExpiration(token: String?, secret: String): Date {
        return extractClaim(token, secret, Function { obj: Claims -> obj.expiration })
    }

    private fun <T> extractClaim(token: String?, secret: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token, secret)
        return claimsResolver.apply(claims)
    }

    private fun extractAllClaims(token: String?, secret: String): Claims {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body
    }

    private fun isTokenExpired(token: String?, secret: String): Boolean? {
        return extractExpiration(token, secret).before(Date())
    }

    fun generateToken(userDetails: UserDetails, expiration: Long, secret: String): String {
        val claims: Map<String, Any> = HashMap()
        return createToken(claims, userDetails.username, expiration, secret)
    }

    private fun createToken(claims: Map<String, Any>, subject: String, expiration: Long, secret: String): String {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret).compact()
    }

    fun validateToken(token: String?, userDetails: UserDetails, secret: String): Boolean {
        val username = extractUsername(token, secret)
        return username == userDetails.username && !isTokenExpired(token, secret)!!
    }
}