package com.example.demo.security.config

import com.example.demo.security.model.token.JwtToken
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties(prefix = "demo.security.jwt")
class JwtSettings {
    /**
     * [JwtToken] will expire after this time.
     */
    var tokenExpirationTime: Long? = null

    /**
     * Token issuer.
     */
    var tokenIssuer: String? = null

    /**
     * Key is used to sign [JwtToken].
     */
    var tokenSigningKey: String? = null

    /**
     * [JwtToken] can be refreshed during this timeframe.
     */
    var refreshTokenExpTime: Long? = null
}