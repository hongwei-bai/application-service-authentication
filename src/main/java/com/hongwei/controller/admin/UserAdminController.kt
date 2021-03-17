package com.hongwei.controller.admin

import com.hongwei.model.contract.AddUserRequest
import com.hongwei.model.contract.Role
import com.hongwei.service.UserAdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
    fun addUser(@RequestBody addUserRequest: AddUserRequest): ResponseEntity<*> {
        userAdminService.addUser(addUserRequest.userName, addUserRequest.passwordHash, Role.valueOf(addUserRequest.role))
        return ResponseEntity.ok(null)
    }

    @RequestMapping(path = ["/deleteUser.do"])
    @ResponseBody
    fun deleteUser(userName: String, sign: String?): ResponseEntity<*> = ResponseEntity.ok(userAdminService.deleteUser(userName))

    @RequestMapping(path = ["/getUsers.do"])
    @ResponseBody
    fun getAllUsers(role: String?): ResponseEntity<*> =
            role?.let {
                ResponseEntity.ok(userAdminService.getUsers(Role.valueOf(role)))
            } ?: ResponseEntity.ok(userAdminService.getUsers(null))

    @RequestMapping(path = ["/getUser.do"])
    @ResponseBody
    fun getUser(userName: String, sign: String?): ResponseEntity<*> =
            ResponseEntity.ok(userAdminService.getUser(userName))

    @RequestMapping(path = ["/updateUser.do"])
    @ResponseBody
    fun updateUser(userName: String, role: String?, passwordHash: String?, sign: String?): ResponseEntity<*> =
            ResponseEntity.ok(userAdminService.updateUser(userName, role, passwordHash))
}