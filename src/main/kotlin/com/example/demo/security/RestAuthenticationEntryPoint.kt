package com.example.demo.security

import java.io.IOException

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class RestAuthenticationEntryPoint : AuthenticationEntryPoint {
    @Throws(IOException::class, ServletException::class)
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, ex: AuthenticationException) {
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized")
    }
}