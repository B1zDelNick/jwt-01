package com.example.demo.security.auth.jwt

import com.example.demo.security.auth.JwtAuthenticationToken
import com.example.demo.security.model.UserContext
import java.util.stream.Collectors
import org.springframework.security.core.authority.SimpleGrantedAuthority
import com.example.demo.security.config.JwtSettings
import com.example.demo.security.model.token.RawAccessJwtToken
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider(
        private val jwtSettings: JwtSettings) : AuthenticationProvider {

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {

        val rawAccessToken = authentication.credentials as RawAccessJwtToken

        val jwsClaims = rawAccessToken.parseClaims(jwtSettings.tokenSigningKey!!)
        val subject = jwsClaims.body.subject
        val scopes: List<String> = jwsClaims.body["scopes"] as List<String>
        val authorities = scopes.stream()
                .map{ authority -> SimpleGrantedAuthority(authority) }
                .collect(Collectors.toList())

        val context = UserContext.create(subject, authorities)

        return JwtAuthenticationToken(context, context.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return JwtAuthenticationToken::class.java!!.isAssignableFrom(authentication)
    }
}