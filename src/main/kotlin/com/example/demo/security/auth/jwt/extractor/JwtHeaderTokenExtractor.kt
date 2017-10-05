package com.example.demo.security.auth.jwt.extractor

import org.apache.commons.lang3.StringUtils.isBlank
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.stereotype.Component


@Component
class JwtHeaderTokenExtractor : TokenExtractor {

    override fun extract(header: String): String {
        if (isBlank(header)) {
            throw AuthenticationServiceException("Authorization header cannot be blank!")
        }

        if (header.length < HEADER_PREFIX.length) {
            throw AuthenticationServiceException("Invalid authorization header size.")
        }

        return header.substring(HEADER_PREFIX.length, header.length)
    }

    companion object {
        var HEADER_PREFIX = "Bearer "
    }
}