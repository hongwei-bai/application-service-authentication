package com.hongwei.service

import com.hongwei.constants.Constants.Security.PublicAccess.PUBLIC_ACCESS_DEFAULT_EXPIRATION_DAY
import com.hongwei.constants.Constants.Security.PublicAccess.PUBLIC_ACCESS_DEFAULT_SECRET
import com.hongwei.constants.Constants.Security.PublicAccess.PUBLIC_ACCESS_STUB_USER
import com.hongwei.constants.Unauthorized
import com.hongwei.security.PublicTokenService
import com.hongwei.security.model.GeneratePublicTokenRequest
import com.hongwei.security.model.GeneratePublicTokenResponse
import com.hongwei.security.model.VerifyPublicTokenRequest
import com.hongwei.security.model.VerifyPublicTokenResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PublicAuthenticateService {
    @Autowired
    private lateinit var publicTokenService: PublicTokenService

    fun verifyPublicToken(request: VerifyPublicTokenRequest): VerifyPublicTokenResponse {
        try {
            val secret = request.secret ?: PUBLIC_ACCESS_DEFAULT_SECRET
            val userName = publicTokenService.extractUserName(request.token!!, secret)

            if (publicTokenService.validateToken(request.token, userName, secret)) {
                val expireDate = publicTokenService.extractExpiration(request.token, secret)
                return VerifyPublicTokenResponse(
                        userName = userName,
                        expirationTimeStamp = expireDate.time,
                        expirationString = expireDate.toString(),
                        secret = secret
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        throw Unauthorized
    }

    fun generatePublicToken(request: GeneratePublicTokenRequest) =
            GeneratePublicTokenResponse(publicTokenService.generateToken(
                    request.userName ?: PUBLIC_ACCESS_STUB_USER,
                    request.expireDays ?: PUBLIC_ACCESS_DEFAULT_EXPIRATION_DAY,
                    request.secret ?: PUBLIC_ACCESS_DEFAULT_SECRET
            ))
}