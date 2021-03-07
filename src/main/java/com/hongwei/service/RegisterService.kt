package com.hongwei.service

import com.hongwei.model.contract.Role
import com.hongwei.model.jpa.User
import com.hongwei.model.jpa.UserRepository
import com.hongwei.service.model.Failure
import com.hongwei.service.model.ServiceResult
import com.hongwei.service.model.Success
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RegisterService {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun registerUser(userName: String, passwordHash: String): ServiceResult {
        userRepository.findByUserName(userName)?.let {
            return Failure("user name already exist")
        }
        val user = User().apply {
            user_name = userName
            password_hash = passwordHash
            role = Role.user.name
        }
        userRepository.save(user)
        return Success<Any>()
    }
}