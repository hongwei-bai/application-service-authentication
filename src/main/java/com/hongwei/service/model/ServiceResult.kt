package com.hongwei.service.model

sealed class ServiceResult

data class Success<T>(val body: T? = null) : ServiceResult()

data class Failure(val message: String? = null) : ServiceResult()