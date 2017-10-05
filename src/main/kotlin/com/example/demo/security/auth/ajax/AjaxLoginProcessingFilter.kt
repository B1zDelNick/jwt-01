package com.example.demo.security.auth.ajax

import com.example.demo.security.error.exceptions.AuthMethodNotSupportedException
import com.example.demo.utils.WebUtil
import org.springframework.security.core.context.SecurityContextHolder
import javax.servlet.ServletException
import java.io.IOException
import javax.servlet.http.HttpServletResponse
import javax.servlet.FilterChain
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.http.HttpMethod
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import javax.servlet.http.HttpServletRequest

class AjaxLoginProcessingFilter(
        defaultProcessUrl: String,
        private val succHandler: AuthenticationSuccessHandler,
        private val failHandler: AuthenticationFailureHandler,
        private val objectMapper: ObjectMapper) : AbstractAuthenticationProcessingFilter(defaultProcessUrl) {

    @Throws(AuthenticationException::class, IOException::class, ServletException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {

        if (HttpMethod.POST.name != request.method || !WebUtil.isAjax(request)) {
            if (logger.isDebugEnabled) {
                logger.debug("Authentication method not supported. Request method: " + request.method)
            }
            throw AuthMethodNotSupportedException("Authentication method not supported")
        }

        val loginRequest = objectMapper.readValue(request.reader, LoginRequest::class.java)

        if (StringUtils.isBlank(loginRequest.username) || StringUtils.isBlank(loginRequest.password)) {
            throw AuthenticationServiceException("Username or Password not provided")
        }

        val token = UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)

        return this.authenticationManager.authenticate(token)
    }

    @Throws(IOException::class, ServletException::class)
    override protected fun successfulAuthentication(
            request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        successHandler.onAuthenticationSuccess(request, response, authResult)
    }

    @Throws(IOException::class, ServletException::class)
    override protected fun unsuccessfulAuthentication(
            request: HttpServletRequest, response: HttpServletResponse, failed: AuthenticationException) {
        SecurityContextHolder.clearContext()
        failureHandler.onAuthenticationFailure(request, response, failed)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AjaxLoginProcessingFilter::class.java)
    }
}