package com.hongwei.security

import com.hongwei.constants.SecurityConfigurations
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function

@Service
class JwtUtil {
    @Autowired
    private lateinit var securityConfigurations: SecurityConfigurations

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
        return Jwts.parser().setSigningKey(securityConfigurations.secret).parseClaimsJws(token).body
    }

    private fun isTokenExpired(token: String?): Boolean? {
        return extractExpiration(token).before(Date())
    }

    fun generateToken(userDetails: UserDetails, expiration: Long): String {
        val claims: Map<String, Any> = HashMap()
        return createToken(claims, userDetails.username, expiration)
    }

    private fun createToken(claims: Map<String, Any>, subject: String, expiration: Long): String {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, securityConfigurations.secret).compact()
    }

    fun validateToken(token: String?, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)!!
    }
}