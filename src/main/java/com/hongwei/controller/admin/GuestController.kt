package com.hongwei.controller.admin

import com.hongwei.service.GuestAdminService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
@CrossOrigin
class GuestController {
    @Autowired
    private lateinit var guestAdminService: GuestAdminService

    @PostMapping(path = ["/guest.do"])
    @ResponseBody
    fun addGuest(description: String, roles: String, expireTime: Long, sign: String?): ResponseEntity<*> =
            ResponseEntity.ok(guestAdminService.addGuest(description, expireTime))

    @DeleteMapping(path = ["/guest.do"])
    @ResponseBody
    fun delete(guestCode: String, sign: String?): ResponseEntity<*> =
            ResponseEntity.ok(guestAdminService.deleteGuest(guestCode))

    @GetMapping(path = ["/guest.do"])
    @ResponseBody
    fun getGuest(guestCode: String, sign: String?): ResponseEntity<*> =
            ResponseEntity.ok(guestAdminService.getGuest(guestCode))

    @PutMapping(path = ["/guest.do"])
    @ResponseBody
    fun updateUser(guestCode: String, expireTime: Long?, sign: String?): ResponseEntity<*> =
            ResponseEntity.ok(guestAdminService.updateGuest(guestCode, expireTime))
}