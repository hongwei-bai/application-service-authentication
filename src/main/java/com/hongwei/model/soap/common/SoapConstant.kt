package com.hongwei.model.soap.common

object SoapConstant {
    const val CODE_ERROR = 1

    //    1xx: Informational
    //    It means the request has been received and the process is continuing.

    //    2	2xx: Success
    //    It means the action was successfully received, understood, and accepted.
    const val CODE_SUCCESS = 200

    //    3	3xx: Redirection
    //    It means further action must be taken in order to complete the request.

    //    4	4xx: Client Error
    //    It means the request contains incorrect syntax or cannot be fulfilled.
    const val CODE_LOGIN_FAILURE = 401

    //    5	5xx: Server Error
    //    It means the server failed to fulfill an apparently valid request.
    const val UNKNOWN_SERVER_ERROR = 500
    const val AUTH_FAILURE = 501


}