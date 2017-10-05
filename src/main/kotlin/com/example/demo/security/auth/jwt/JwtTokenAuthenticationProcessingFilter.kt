package com.example.demo.security.auth.jwt

import org.springframework.security.core.context.SecurityContextHolder
import javax.servlet.ServletException
import java.io.IOException
import javax.servlet.http.HttpServletResponse
import javax.servlet.FilterChain
import com.example.demo.security.auth.JwtAuthenticationToken
import com.example.demo.security.auth.jwt.extractor.TokenExtractor
import com.example.demo.security.model.token.RawAccessJwtToken
import com.example.demo.security.config.WebSecurityConfig
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.servlet.http.HttpServletRequest

class JwtTokenAuthenticationProcessingFilter(
        private val failHandler: AuthenticationFailureHandler,
        private val tokenExtractor: TokenExtractor,
        matcher: RequestMatcher) : AbstractAuthenticationProcessingFilter(matcher) {

    @Throws(AuthenticationException::class, IOException::class, ServletException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        val tokenPayload = request.getHeader(WebSecurityConfig.JWT_TOKEN_HEADER_PARAM)
        val token = RawAccessJwtToken(tokenExtractor.extract(tokenPayload))
        return authenticationManager.authenticate(JwtAuthenticationToken(token))
    }

    @Throws(IOException::class, ServletException::class)
    override protected fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain,
                                           authResult: Authentication) {
        val context = SecurityContextHolder.createEmptyContext()
        context.authentication = authResult
        SecurityContextHolder.setContext(context)
        chain.doFilter(request, response)
    }

    @Throws(IOException::class, ServletException::class)
    override protected fun unsuccessfulAuthentication(
            request: HttpServletRequest, response: HttpServletResponse, failed: AuthenticationException) {
        SecurityContextHolder.clearContext()
        failureHandler.onAuthenticationFailure(request, response, failed)
    }
}