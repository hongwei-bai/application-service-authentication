package com.hongwei.enums.common

import java.util.*

enum class ResponseResultEnum {
    SUCCESS(0, "success", "调用成功"), FAILED(1, "failed", "基础服务平台调用失败"), PARAM_NULL(2, "param_null", "参数为空"), PARAM_WRONG(3, "param_wrong", "参数有误"), MOBILE_PLATFORM_WRONG(4, "mobile_platform_wrong", "组件平台调用失败"), HINT_MSG(5, "hint_msg", "提示信息");

    private var code: Int? = null
    private var message: String? = null
    private var description: String

    /**
     * @param description 中文描述
     */
    constructor(description: String) {
        this.description = description
    }

    /**
     * @param code        数字编码
     * @param description 中文描述
     */
    constructor(code: Int, description: String) {
        this.code = code
        this.description = description
    }

    /**
     * @param message     英文编码名称
     * @param description 中文描述
     */
    constructor(message: String, description: String) {
        this.message = name
        this.description = description
    }

    /**
     * @param code        数字编码
     * @param message     英文编码名称
     * @param description 中文描述
     */
    constructor(code: Int, message: String, description: String) {
        this.code = code
        this.message = message
        this.description = description
    }

    /**
     * 获取枚举类型数值编码
     */
    fun toCode(): Int {
        return if (code == null) ordinal else code!!
    }

    /**
     * 获取枚举类型英文编码名称
     */
    fun toName(): String? {
        return if (name == null) name else name
    }

    /**
     * 获取枚举类型中文描述
     */
    fun toDescription(): String {
        return description
    }

    /**
     * 获取枚举类型中文描述
     */
    override fun toString(): String {
        return description
    }

    companion object {
        /**
         * 按数值获取对应的枚举类型
         *
         * @param code 数值
         * @return 枚举类型
         */
        fun enumValueOf(code: Int): ResponseResultEnum? {
            val values = values()
            var v: ResponseResultEnum? = null
            for (i in values.indices) {
                if (values[i].toCode() == code) {
                    v = values[i]
                    break
                }
            }
            return v
        }

        /**
         * 按英文编码获取对应的枚举类型
         *
         * @param name 英文编码
         * @return 枚举类型
         */
        fun enumValueOf(name: String?): ResponseResultEnum? {
            val values = values()
            var v: ResponseResultEnum? = null
            for (i in values.indices) {
                if (values[i].toName().equals(name, ignoreCase = true)) {
                    v = values[i]
                    break
                }
            }
            return v
        }

        /**
         * 获取枚举类型的所有<数字编码></数字编码>,中文描述>对
         *
         * @return
         */
        fun toCodeDescriptionMap(): TreeMap<Int, String> {
            val map = TreeMap<Int, String>()
            for (i in values().indices) {
                map[values()[i].toCode()] = values()[i].toDescription()
            }
            return map
        }

        /**
         * 获取枚举类型的所有<英文编码名称></英文编码名称>,中文描述>对
         *
         * @return
         */
        fun toNameDescriptionMap(): TreeMap<String?, String> {
            val map = TreeMap<String?, String>()
            for (i in values().indices) {
                map[values()[i].toName()] = values()[i].toDescription()
            }
            return map
        }
    }
}