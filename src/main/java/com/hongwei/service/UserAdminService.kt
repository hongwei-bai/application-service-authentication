package com.hongwei.service

import com.hongwei.constants.Conflict
import com.hongwei.constants.NotFound
import com.hongwei.controller.admin.UserAdminController
import com.hongwei.model.contract.Role
import com.hongwei.model.jpa.GuestRepository
import com.hongwei.model.jpa.User
import com.hongwei.model.jpa.UserRepository
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
            stringBuilder.append(" $index | ${user.userName} | ${user.credential}\n")
        }

        stringBuilder.append("table Guest: id |  guest_code | expire_time\n")
        guestRepository.findAll().forEachIndexed { index, guest ->
            stringBuilder.append(" $index | ${guest.guestCode} | ${guest.expireTime}\n")
        }

        return stringBuilder.toString()
    }

    fun addUser(userName: String, passwordHash: String, role: Role) {
        userRepository.findByUserName(userName)?.let {
            throw Conflict
        }
        userRepository.save(User().apply {
            this.userName = userName
            this.credential = passwordHash
            this.role = role.name
        })
    }

    fun deleteUser(userName: String) {
        userRepository.findByUserName(userName) ?: throw NotFound
        userRepository.deleteByUserName(userName)
    }

    fun getUsers(role: Role? = null): List<User> {
        if (role != null) {
            return userRepository.findUsersByRole(role.name)
        }

        val list = mutableListOf<User>()
        userRepository.findUsersByRole(Role.admin.name)?.let {
            list.addAll(it)
        }
        userRepository.findUsersByRole(Role.user.name)?.let {
            list.addAll(it)
        }
        return list
    }

    fun getUser(userName: String): User = userRepository.findByUserName(userName) ?: throw NotFound

    fun updateUser(userName: String, role: String?, passwordHash: String?) {
        val user = userRepository.findByUserName(userName) ?: throw NotFound
        userRepository.save(user.apply {
            role?.let { this.role = role }
            passwordHash?.let { credential = passwordHash }
        })
    }
}