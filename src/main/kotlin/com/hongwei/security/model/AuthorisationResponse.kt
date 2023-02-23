package com.hongwei.security.model

data class AuthorisationResponse(
        val validated: Boolean,
        val validatedUntil: Long = LONG_TERM,
        val userName: String,
        val role: String,
        val preferenceJson: String,
        val privilegeJson: String
)

const val LONG_TERM = -1L