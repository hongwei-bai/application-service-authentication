package com.hongwei.service

import com.hongwei.controller.admin.UserAdminController
import com.hongwei.model.contract.Role
import com.hongwei.model.jpa.GuestRepository
import com.hongwei.model.jpa.User
import com.hongwei.model.jpa.UserRepository
import com.hongwei.service.model.Failure
import com.hongwei.service.model.ServiceResult
import com.hongwei.service.model.Success
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserAdminService {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var guestRepository: GuestRepository

    private val logger: Logger = LoggerFactory.getLogger(UserAdminController::class.java)

    fun test(): String {
        val userCount = userRepository.count()
        val guestCount = guestRepository.count()

        val stringBuilder = StringBuilder("userCount: $userCount\nguestCount: $guestCount\n")

        stringBuilder.append("table User: id |  user_name | password_hash\n")
        userRepository.findAll().forEachIndexed { index, user ->
            stringBuilder.append(" $index | ${user.user_name} | ${user.password_hash}\n")
        }

        stringBuilder.append("table Guest: id |  guest_code | expire_time\n")
        guestRepository.findAll().forEachIndexed { index, guest ->
            stringBuilder.append(" $index | ${guest.guest_code} | ${guest.expire_time}\n")
        }

        return stringBuilder.toString()
    }

    fun addUser(userName: String, passwordHash: String, role: Role): ServiceResult {
        userRepository.findByUserName(userName)?.let {
            return Failure("user already exists")
        }
        userRepository.save(User().apply {
            user_name = userName
            password_hash = passwordHash
            this.role = role.name
        })
        return Success<Any>()
    }

    fun deleteUser(userName: String): ServiceResult {
        userRepository.findByUserName(userName)
                ?: return Failure("User does not exist")
        userRepository.deleteByUserName(userName)
        return Success<Any>()
    }

    fun getUsers(role: Role? = null): ServiceResult {
        if (role != null) {
            return userRepository.findUsersByRole(role.name)?.let {
                Success(it)
            } ?: Failure("No user acting role ${role.name}")
        }

        val list = mutableListOf<User>()
        userRepository.findUsersByRole(Role.admin.name)?.let {
            list.addAll(it)
        }
        userRepository.findUsersByRole(Role.user.name)?.let {
            list.addAll(it)
        }
        return Success(list)
    }

    fun getUser(userName: String): ServiceResult =
            userRepository.findByUserName(userName)?.let {
                Success(it)
            } ?: Failure("User does not exist")

    fun updateUser(userName: String, role: String?, passwordHash: String?): ServiceResult {
        val user = userRepository.findByUserName(userName)
                ?: return Failure("User does not exist")
        userRepository.save(user.apply {
            role?.let { this.role = role }
            passwordHash?.let { password_hash = passwordHash }
        })
        return Success<Any>()
    }
}