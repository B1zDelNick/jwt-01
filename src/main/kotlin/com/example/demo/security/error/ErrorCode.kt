package com.example.demo.security.error

import com.fasterxml.jackson.annotation.JsonValue


enum class ErrorCode private constructor(@get:JsonValue val errorCode: Int) {

    GLOBAL(2),
    AUTHENTICATION(10),
    JWT_TOKEN_EXPIRED(11)
}