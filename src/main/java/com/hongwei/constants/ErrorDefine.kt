package com.hongwei.constants

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(value = HttpStatus.BAD_REQUEST)
object BadRequest : RuntimeException()

@ResponseStatus(value = HttpStatus.FORBIDDEN)
object Forbidden : RuntimeException()

@ResponseStatus(value = HttpStatus.NOT_FOUND)
object NotFound : RuntimeException()

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
object Unauthorized : RuntimeException()

@ResponseStatus(value = HttpStatus.NON_AUTHORITATIVE_INFORMATION)
object NonAuthoritative : RuntimeException()

@ResponseStatus(value = HttpStatus.NO_CONTENT)
object NoContent : RuntimeException()

@ResponseStatus(value = HttpStatus.NOT_MODIFIED)
object NotModified : RuntimeException()

@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
object MethodNotAllowed : RuntimeException()

@ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT)
object RequestTimeout : RuntimeException()

@ResponseStatus(value = HttpStatus.CONFLICT)
object Conflict : RuntimeException()

@ResponseStatus(value = HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
object RequestedRangeNotSatisfiable : RuntimeException()

@ResponseStatus(value = HttpStatus.LOCKED)
object Locked : RuntimeException()

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
object InternalServerError : RuntimeException()

@ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
object NotImplemented : RuntimeException()

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
object ServiceUnavailable : RuntimeException()