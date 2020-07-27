package com.hongwei.controller.admin

import com.google.gson.Gson
import com.hongwei.Constant
import com.hongwei.model.jpa.Guest
import com.hongwei.model.jpa.GuestRepository
import com.hongwei.model.soap.common.Response
import com.hongwei.model.soap.common.SoapConstant.CODE_ERROR
import com.hongwei.model.soap.common.SoapConstant.CODE_SUCCESS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import kotlin.random.Random


@RestController
@RequestMapping("/admin")
@CrossOrigin
class GuestController {
    @Autowired
    private lateinit var guestRepository: GuestRepository

    private val tmpToken: String = getTmpToken()

    @PostMapping(path = ["/guest.do"])
    @ResponseBody
    fun addGuest(description: String, roles: String, expireTime: Long, sign: String?): String {
        var guestCode = generateGuestCode()
        while (guestRepository.findByGuestCode(guestCode) != null) {
            guestCode = generateGuestCode()
        }
        val guest = Guest().apply {
            this.description = description
            guest_code = guestCode
            expire_time = expireTime
            this.roles = roles
            token = tmpToken
        }
        guestRepository.save(guest)
        return Gson().toJson(Response.from(CODE_SUCCESS, "guest create succeed.", guest))
    }

    @DeleteMapping(path = ["/guest.do"])
    @ResponseBody
    fun delete(guestCode: String, sign: String?): String {
        guestRepository.findByGuestCode(guestCode)
                ?: return Gson().toJson(Response.from(CODE_ERROR, "guest does not exists!"))
        guestRepository.deleteByGuestCode(guestCode)
        return Gson().toJson(Response.from(CODE_SUCCESS, "guest delete succeed."))
    }

    @GetMapping(path = ["/guest.do"])
    @ResponseBody
    fun getUser(guestCode: String?, sign: String?): String {
        return Gson().toJson(Response.from(CODE_ERROR, "get guest succeed.", guestRepository.findByGuestCode(guestCode)))
    }

    @PutMapping(path = ["/guest.do"])
    @ResponseBody
    fun updateUser(guestCode: String?, roles: String?, expireTime: Long?, sign: String?): String {
        val guest = guestRepository.findByGuestCode(guestCode)
                ?: return Gson().toJson(Response.from(CODE_ERROR, "user does not exists!"))
        guestRepository.save(guest.apply {
            roles?.let { this.roles = roles }
            expireTime?.let { expire_time = expireTime }
        })

        return Gson().toJson(Response.from(CODE_ERROR, "update user succeed."))
    }

    private fun generateGuestCode(): String {
        val length = table.size
        var seed = Random(System.currentTimeMillis() + System.nanoTime()).nextInt(length)
        val stringBuilder = StringBuilder()
        for (i in 0..5) {
            seed = Random((System.currentTimeMillis() + System.nanoTime()) * seed).nextInt(length)
            stringBuilder.append("${table[seed]}")
        }
        return stringBuilder.toString()
    }

    private fun getTmpToken(): String {
//        val mapper = ObjectMapper(YAMLFactory())
//        mapper.findAndRegisterModules()
//        val bean: AuthYml = mapper.readValue(File("src/main/resources/auth.yaml"), AuthYml::class.java)
//        return bean.token
        return Constant.ACCESS_TOKEN
    }

    companion object {
        val table = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z')
    }
}