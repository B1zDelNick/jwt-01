package com.example.demo.security.error

import org.springframework.http.HttpStatus
import java.util.*


class ErrorResponse
    protected constructor(// General Error message
        val message: String, // Error code
        val errorCode: ErrorCode, // HTTP Response Status Code
        private val status: HttpStatus) {

    val timestamp: Date

    init {
        this.timestamp = java.util.Date()
    }

    fun getStatus(): Int {
        return status.value()
    }

    companion object {

        fun of(message: String, errorCode: ErrorCode, status: HttpStatus): ErrorResponse {
            return ErrorResponse(message, errorCode, status)
        }
    }
}