package com.hongwei.controller.admin

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hongwei.controller.model.ErrorCode.CODE_ERROR
import com.hongwei.controller.model.ErrorCode.CODE_SUCCESS
import com.hongwei.controller.model.Response
import com.hongwei.model.jpa.Guest
import com.hongwei.service.GuestAdminService
import com.hongwei.service.model.AddGuestResponseBody
import com.hongwei.service.model.Failure
import com.hongwei.service.model.Success
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
@CrossOrigin
class GuestController {
    @Autowired
    private lateinit var guestAdminService: GuestAdminService

    @PostMapping(path = ["/guest.do"])
    @ResponseBody
    fun addGuest(description: String, roles: String, expireTime: Long, sign: String?): String =
            when (val result = guestAdminService.addGuest(description, expireTime)) {
                is Success<*> -> Gson().toJson(Response<AddGuestResponseBody>().apply {
                    code = CODE_SUCCESS
                    msg = "Add guest success"
                    data = result.body as AddGuestResponseBody
                }, object : TypeToken<Response<AddGuestResponseBody?>>() {}.type)
                is Failure -> Gson().toJson(Response.from(CODE_ERROR, result.message ?: ""))
            }

    @DeleteMapping(path = ["/guest.do"])
    @ResponseBody
    fun delete(guestCode: String, sign: String?): String =
            when (val result = guestAdminService.deleteGuest(guestCode)) {
                is Success<*> -> Gson().toJson(Response<Any>().apply {
                    code = CODE_SUCCESS
                    msg = "Delete guest success"
                }, object : TypeToken<Response<Any>>() {}.type)
                is Failure -> Gson().toJson(Response.from(CODE_ERROR, result.message ?: ""))
            }

    @GetMapping(path = ["/guest.do"])
    @ResponseBody
    fun getGuest(guestCode: String, sign: String?): String =
            when (val result = guestAdminService.getGuest(guestCode)) {
                is Success<*> -> Gson().toJson(Response<Guest>().apply {
                    code = CODE_SUCCESS
                    msg = "Add guest success"
                    data = result.body as Guest
                }, object : TypeToken<Response<Guest>>() {}.type)
                is Failure -> Gson().toJson(Response.from(CODE_ERROR, result.message ?: ""))
            }

    @PutMapping(path = ["/guest.do"])
    @ResponseBody
    fun updateUser(guestCode: String, description: String?, expireTime: Long?, sign: String?): String =
            if (description != null || expireTime != null) {
                when (val result = guestAdminService.updateGuest(guestCode, description, expireTime)) {
                    is Success<*> -> Gson().toJson(Response<Any>().apply {
                        code = CODE_SUCCESS
                        msg = "Update guest success"
                    }, object : TypeToken<Response<Any>>() {}.type)
                    is Failure -> Gson().toJson(Response.from(CODE_ERROR, result.message ?: ""))
                }
            } else {
                Gson().toJson(Response.from(CODE_ERROR, "Nothing to update"))
            }
}