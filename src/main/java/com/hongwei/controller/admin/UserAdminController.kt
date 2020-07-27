package com.hongwei.controller.admin

import com.google.gson.Gson
import com.hongwei.Constant
import com.hongwei.model.contract.Roles
import com.hongwei.model.jpa.GuestRepository
import com.hongwei.model.jpa.User
import com.hongwei.model.jpa.UserRepository
import com.hongwei.model.soap.common.Response
import com.hongwei.model.soap.common.SoapConstant.CODE_ERROR
import com.hongwei.model.soap.common.SoapConstant.CODE_SUCCESS
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/admin")
@CrossOrigin
class UserAdminController {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var guestRepository: GuestRepository

    private val tmpToken: String = getTmpToken()

    private val logger: Logger = LoggerFactory.getLogger(UserAdminController::class.java)

    @RequestMapping(path = ["/test.do"])
    @ResponseBody
    fun test(): String {
        val userCount = userRepository.count()
        val guestCount = guestRepository.count()

        logger.debug("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx userCount: $userCount, guestCount: $guestCount")

        val stringBuilder = StringBuilder("userCount: $userCount\nguestCount: $guestCount\n")

        stringBuilder.append("table User: id |  user_name | password_hash | token | roles\n")
        userRepository.findAll().forEachIndexed { index, user ->
            stringBuilder.append(" $index | ${user.user_name} | ${user.password_hash} | ${user.token} | ${user.roles}\n")
        }

        stringBuilder.append("table Guest: id |  guest_code | expire_time | token | roles\n")
        guestRepository.findAll().forEachIndexed { index, guest ->
            stringBuilder.append(" $index | ${guest.guest_code} | ${guest.expire_time} | ${guest.token} | ${guest.roles}\n")
        }

        return stringBuilder.toString()
    }

    @RequestMapping(path = ["/addUser.do"])
    @ResponseBody
    fun addUser(args: String, token: String?, sign: String?): String {


//        val obj = Gson().fromJson(args, AddUserRequest::class.java)
//        userRepository.findByUserName(obj.userName)?.let {
//            return Gson().toJson(Response.from(CODE_ERROR, "user already exists!"))
//        }
//        userRepository.save(User().apply {
//            user_name = obj.userName
//            password_hash = obj.passwordHash
//            this.roles = roles
//            this.token = tmpToken
//        })
        return Gson().toJson(Response.from(CODE_SUCCESS, "user create succeed."))
    }

    @RequestMapping(path = ["/removeUser.do"])
    @ResponseBody
    fun deleteUser(userName: String, token: String?, sign: String?): String {
        userRepository.findByUserName(userName)
                ?: return Gson().toJson(Response.from(CODE_ERROR, "user does not exists!"))
        userRepository.deleteByUserName(userName)
        return Gson().toJson(Response.from(CODE_SUCCESS, "user delete succeed."))
    }

    @RequestMapping(path = ["/getAllUsers.do"])
    @ResponseBody
    fun getAllUsers(token: String?, sign: String?): String {
        return Gson().toJson(Response.from(CODE_ERROR, "get users succeed.", emptyList<User>()))
    }

    @RequestMapping(path = ["/getUser.do"])
    @ResponseBody
    fun getUser(userName: String?, token: String?, sign: String?): String {
        return Gson().toJson(Response.from(CODE_ERROR, "get user succeed.", userRepository.findByUserName(userName)))
    }

    @RequestMapping(path = ["/updateUser.do"])
    @ResponseBody
    fun updateUser(userName: String?, roles: String?, passwordHash: String?, token: String?, sign: String?): String {
        val user = userRepository.findByUserName(userName)
                ?: return Gson().toJson(Response.from(CODE_ERROR, "user does not exists!"))
        userRepository.save(user.apply {
            roles?.let { this.roles = roles }
            passwordHash?.let { password_hash = passwordHash }
        })

        return Gson().toJson(Response.from(CODE_ERROR, "update user succeed."))
    }

    private fun getUser(token: String): User? {
        return userRepository.findByUserToken(token)
    }

    private fun isValidAdmin(token: String): Boolean {
        return getUser(token)?.roles == Roles.Administrator.name
    }

    private fun getTmpToken(): String {
        return Constant.ACCESS_TOKEN
    }
}