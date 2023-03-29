package com.devaffeine.auth.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
internal class InvalidCredentialsException : Exception {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}