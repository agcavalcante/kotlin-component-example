package com.example.demo.exceptions

import com.example.demo.data.ErrorsDetails
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*

@ControllerAdvice
class ControllerAdvisor : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [(NoContentException::class)])
    fun handleConflict(exception: NoContentException, request: WebRequest): ResponseEntity<ErrorsDetails> {
        val errorsDetails = ErrorsDetails(Date(), "No content to show", exception.message!!)
        return ResponseEntity(errorsDetails, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [(IllegalQuantityException::class)])
    fun handleConflict(exception: IllegalQuantityException, request: WebRequest): ResponseEntity<ErrorsDetails> {
        val errorsDetails = ErrorsDetails(Date(), "Wrong value to parameter", exception.message!!)
        return ResponseEntity(errorsDetails, HttpStatus.NOT_ACCEPTABLE)
    }

    @ExceptionHandler(value = [(EmptyResultDataAccessException::class)])
    fun handleConflict(exception: EmptyResultDataAccessException, request: WebRequest): ResponseEntity<ErrorsDetails> {
        val errorsDetails = ErrorsDetails(Date(), "No content to show", "There is no data for the searched id")
        return ResponseEntity(errorsDetails, HttpStatus.NOT_FOUND)
    }
}