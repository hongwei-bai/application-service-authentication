package com.hongwei.controller.admin

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hongwei.controller.model.ErrorCode.CODE_ERROR
import com.hongwei.controller.model.ErrorCode.CODE_SUCCESS
import com.hongwei.controller.model.Response
import com.hongwei.model.contract.Role
import com.hongwei.model.jpa.User
import com.hongwei.service.UserAdminService
import com.hongwei.service.model.Failure
import com.hongwei.service.model.Success
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
    private lateinit var userAdminService: UserAdminService

    @RequestMapping(path = ["/test.do"])
    @ResponseBody
    fun test(): String = userAdminService.test()

    @RequestMapping(path = ["/addUser.do"])
    @ResponseBody
    fun addUser(userName: String, passwordHash: String, role: String = Role.user.name, sign: String?): String =
            when (val result = userAdminService.addUser(userName, passwordHash, Role.valueOf(role))) {
                is Success<*> -> Gson().toJson(Response<Any>().apply {
                    code = CODE_SUCCESS
                    msg = "Add user success"
                }, object : TypeToken<Response<*>>() {}.type)
                is Failure -> Gson().toJson(Response.from(CODE_ERROR, result.message ?: ""))
            }

    @RequestMapping(path = ["/deleteUser.do"])
    @ResponseBody
    fun deleteUser(userName: String, sign: String?): String =
            when (val result = userAdminService.deleteUser(userName)) {
                is Success<*> -> Gson().toJson(Response<Any>().apply {
                    code = CODE_SUCCESS
                    msg = "Delete user success"
                }, object : TypeToken<Response<*>>() {}.type)
                is Failure -> Gson().toJson(Response.from(CODE_ERROR, result.message ?: ""))
            }

    @RequestMapping(path = ["/getUsers.do"])
    @ResponseBody
    fun getAllUsers(role: String?): String {
        var roleObject: Role? = null
        if (role != null) {
            try {
                roleObject = Role.valueOf(role)
            } catch (e: IllegalArgumentException) {
                return Gson().toJson(Response.from(CODE_ERROR, "Invalid role"))
            }
        }

        return when (val result = userAdminService.getUsers(roleObject)) {
            is Success<*> -> Gson().toJson(Response<List<User>>().apply {
                code = CODE_SUCCESS
                msg = "Get users success"
                data = result.body as List<User>
            }, object : TypeToken<Response<List<User>>>() {}.type)
            is Failure -> Gson().toJson(Response.from(CODE_ERROR, result.message ?: ""))
        }
    }

    @RequestMapping(path = ["/getUser.do"])
    @ResponseBody
    fun getUser(userName: String, sign: String?): String =
            when (val result = userAdminService.getUser(userName)) {
                is Success<*> -> Gson().toJson(Response<User>().apply {
                    code = CODE_SUCCESS
                    msg = "Get user success"
                    data = result.body as User
                }, object : TypeToken<Response<*>>() {}.type)
                is Failure -> Gson().toJson(Response.from(CODE_ERROR, result.message ?: ""))
            }

    @RequestMapping(path = ["/updateUser.do"])
    @ResponseBody
    fun updateUser(userName: String, role: String?, passwordHash: String?, sign: String?): String =
            if (role != null || passwordHash != null) {
                when (val result = userAdminService.updateUser(userName, role, passwordHash)) {
                    is Success<*> -> Gson().toJson(Response<Any>().apply {
                        code = CODE_SUCCESS
                        msg = "Update user success"
                    }, object : TypeToken<Response<Any>>() {}.type)
                    is Failure -> Gson().toJson(Response.from(CODE_ERROR, result.message ?: ""))
                }
            } else {
                Gson().toJson(Response.from(CODE_ERROR, "Nothing to update"))
            }
}