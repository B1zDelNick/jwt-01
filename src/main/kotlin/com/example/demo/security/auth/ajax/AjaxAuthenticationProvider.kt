package com.example.demo.security.auth.ajax

import com.example.demo.security.model.UserContext
import com.example.demo.security.model.service.DatabaseUserService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import java.util.stream.Collectors
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import org.springframework.util.Assert


@Component
class AjaxAuthenticationProvider(
        private val userService: DatabaseUserService,
        private val encoder: BCryptPasswordEncoder) : AuthenticationProvider {

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        Assert.notNull(authentication, "No authentication data provided")

        val username = authentication.principal as String
        val password = authentication.credentials as String

        val user = userService.getByUsername(username)
                .orElseThrow({ UsernameNotFoundException("User not found: " + username) })

        if (!encoder.matches(password, user.password)) {
            throw BadCredentialsException("Authentication Failed. Username or Password not valid.")
        }

        if (user.roles == null) throw InsufficientAuthenticationException("User has no roles assigned")

        val authorities = user.roles!!.stream()
                .map{ authority -> SimpleGrantedAuthority(authority.role.authority()) }
                .collect(Collectors.toList())

        val userContext = UserContext.create(user.username, authorities)

        return UsernamePasswordAuthenticationToken(userContext, null, userContext.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}