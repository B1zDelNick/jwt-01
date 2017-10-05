package com.example.demo.security.auth.ajax

import com.example.demo.security.error.exceptions.AuthMethodNotSupportedException
import com.example.demo.security.error.ErrorCode.*
import com.example.demo.security.error.ErrorResponse
import com.example.demo.security.error.exceptions.JwtExpiredTokenException
import org.springframework.security.authentication.BadCredentialsException
import javax.servlet.ServletException
import java.io.IOException
import javax.servlet.http.HttpServletResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest


@Component
class AjaxAwareAuthenticationFailureHandler(
        private val mapper: ObjectMapper) : AuthenticationFailureHandler {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(
            request: HttpServletRequest, response: HttpServletResponse, e: AuthenticationException) {

        response.status = UNAUTHORIZED.value()
        response.contentType = APPLICATION_JSON_VALUE

        when (e) {
            is BadCredentialsException -> mapper.writeValue(response.writer, ErrorResponse.of("Invalid username or password", AUTHENTICATION, UNAUTHORIZED))
            is JwtExpiredTokenException -> mapper.writeValue(response.writer, ErrorResponse.of("Token has expired", JWT_TOKEN_EXPIRED, UNAUTHORIZED))
            is AuthMethodNotSupportedException -> mapper.writeValue(response.writer, ErrorResponse.of(e.message!!, AUTHENTICATION, UNAUTHORIZED))
        }

        mapper.writeValue(response.writer, ErrorResponse.of("Authentication failed", AUTHENTICATION, UNAUTHORIZED))
    }
}