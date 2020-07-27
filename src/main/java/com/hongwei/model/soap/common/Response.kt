package com.hongwei.model.soap.common

import com.fasterxml.jackson.annotation.JsonInclude
import com.hongwei.model.soap.common.SoapConstant.CODE_SUCCESS
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
class Response<T>(
        var code: Int = CODE_SUCCESS,
        var msg: String = "",
        var data: T? = null
) : Serializable {
    companion object {
        fun from(code: Int): Response<*> {
            return Response<Any?>(code, "", null)
        }

        fun from(code: Int, msg: String): Response<*> {
            return Response<Any?>(code, msg, null)
        }

        fun from(code: Int, msg: String, data: Any): Response<*> {
            return Response<Any?>(code, msg, data)
        }
    }
}