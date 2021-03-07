package com.hongwei.service

import com.hongwei.model.jpa.Guest
import com.hongwei.model.jpa.GuestRepository
import com.hongwei.service.helper.GuestCodeGenerator
import com.hongwei.service.model.AddGuestResponseBody
import com.hongwei.service.model.Failure
import com.hongwei.service.model.ServiceResult
import com.hongwei.service.model.Success
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GuestAdminService {
    @Autowired
    private lateinit var guestRepository: GuestRepository

    fun addGuest(description: String, expireTime: Long): ServiceResult {
        var guestCode = GuestCodeGenerator.generate()
        while (guestRepository.findByGuestCode(guestCode) != null) {
            guestCode = GuestCodeGenerator.generate()
        }
        val guest = Guest().apply {
            this.description = description
            guest_code = guestCode
            expire_time = expireTime
        }
        guestRepository.save(guest)
        return Success(AddGuestResponseBody(guestCode))
    }

    fun deleteGuest(guestCode: String): ServiceResult {
        guestRepository.findByGuestCode(guestCode) ?: return Failure("Guest code does not exist")
        guestRepository.deleteByGuestCode(guestCode)
        return Success<Any>()
    }

    fun getGuest(guestCode: String): ServiceResult =
            guestRepository.findByGuestCode(guestCode)?.let {
                Success(it)
            } ?: Failure("Guest code does not exist")

    fun updateGuest(guestCode: String, newDescription: String?, newExpireTime: Long?): ServiceResult {
        val guest = guestRepository.findByGuestCode(guestCode) ?: return Failure("Guest code does not exist")
        guestRepository.save(guest.apply {
            newExpireTime?.let { this.expire_time = newExpireTime }
            newDescription?.let { this.description = newDescription }
        })
        return Success<Any>()
    }
}