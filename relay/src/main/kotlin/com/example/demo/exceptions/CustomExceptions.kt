package com.example.demo.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NO_CONTENT)
class NoContentException(exceptionMessage: String) : RuntimeException(exceptionMessage)

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
class IllegalCpfFormatException(exceptionMessage: String) : RuntimeException(exceptionMessage)