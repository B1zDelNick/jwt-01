package com.example.demo.security.auth.ajax

import org.springframework.security.web.WebAttributes
import javax.servlet.http.HttpSession
import org.apache.catalina.manager.StatusTransformer.setContentType
import org.springframework.http.HttpStatus
import java.util.HashMap
import com.example.demo.security.model.UserContext
import com.example.demo.security.model.token.JwtTokenFactory
import javax.servlet.ServletException
import java.io.IOException
import javax.servlet.http.HttpServletResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest


@Component
class AjaxAwareAuthenticationSuccessHandler(
        private val mapper: ObjectMapper,
        private val tokenFactory: JwtTokenFactory) : AuthenticationSuccessHandler {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
            request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {

        val userContext = authentication.principal as UserContext

        val accessToken = tokenFactory.createAccessJwtToken(userContext)
        val refreshToken = tokenFactory.createRefreshToken(userContext)

        val tokenMap = HashMap<String, String>()
        tokenMap.put("token", accessToken.token)
        tokenMap.put("refreshToken", refreshToken.token)

        response.status = HttpStatus.OK.value()
        response.contentType = APPLICATION_JSON_VALUE
        mapper.writeValue(response.writer, tokenMap)

        clearAuthenticationAttributes(request)
    }

    /**
     * Removes temporary authentication-related data which may have been stored
     * in the session during the authentication process..
     *
     */
    protected fun clearAuthenticationAttributes(request: HttpServletRequest) {
        val session = request.getSession(false) ?: return
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)
    }
}