package com.hongwei.security.model

data class AuthorisationResponse(
        val validated: Boolean,
        val userName: String,
        val role: String,
        val preferenceJson: String
)