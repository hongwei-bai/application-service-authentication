package com.hongwei.security.model

data class VerifyPublicTokenResponse(
        val userName: String,
        val expirationTimeStamp: Long,
        val expirationString: String,
        val secret: String
)