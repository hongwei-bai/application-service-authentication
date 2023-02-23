package com.hongwei.service

import com.hongwei.constants.NotFound
import com.hongwei.constants.SecurityConfigurations
import com.hongwei.model.jpa.Guest
import com.hongwei.model.jpa.GuestRepository
import com.hongwei.service.helper.GuestCodeGenerator
import com.hongwei.service.model.AddGuestResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GuestAdminService {
    @Autowired
    private lateinit var guestRepository: GuestRepository

    @Autowired
    private lateinit var securityConfigurations: SecurityConfigurations

    fun addGuest(description: String, expireTime: Long): AddGuestResponse {
        var guestCode = GuestCodeGenerator.generate(securityConfigurations.guestCodeLength)
        while (guestRepository.findByGuestCode(guestCode) != null) {
            guestCode = GuestCodeGenerator.generate(securityConfigurations.guestCodeLength)
        }
        val guest = Guest().apply {
            this.guestCode = guestCode
            this.expireTime = expireTime
        }
        guestRepository.save(guest)
        return AddGuestResponse(guestCode)
    }

    fun deleteGuest(guestCode: String) {
        guestRepository.findByGuestCode(guestCode) ?: throw NotFound
        guestRepository.deleteByGuestCode(guestCode)
    }

    fun getGuest(guestCode: String): Guest = guestRepository.findByGuestCode(guestCode) ?: throw NotFound

    fun getAllGuests(guestCode: String): List<Guest> = guestRepository.findAllGuest()

    fun updateGuest(guestCode: String, newExpireTime: Long?) {
        val guest = guestRepository.findByGuestCode(guestCode) ?: throw NotFound
        guestRepository.save(guest.apply {
            newExpireTime?.let { this.expireTime = newExpireTime }
        })
    }
}