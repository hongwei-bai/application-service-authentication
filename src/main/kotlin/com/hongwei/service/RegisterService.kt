package com.hongwei.service

import com.hongwei.constants.Conflict
import com.hongwei.model.contract.Role
import com.hongwei.model.jpa.User
import com.hongwei.model.jpa.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RegisterService {
    @Autowired
    private lateinit var userRepository: UserRepository

    fun registerUser(userName: String, passwordHash: String) {
        userRepository.findByUserName(userName)?.let {
            throw Conflict
        }
        val user = User().apply {
            this.userName = userName
            this.credential = passwordHash
            this.role = Role.user.name
        }
        userRepository.save(user)
    }
}