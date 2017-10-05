package com.example.demo.security.model.token

import com.example.demo.security.config.JwtSettings
import com.example.demo.security.model.Scopes
import java.time.ZoneId
import io.jsonwebtoken.Jwts
import java.time.LocalDateTime
import com.example.demo.security.model.UserContext
import io.jsonwebtoken.SignatureAlgorithm
import org.apache.commons.lang3.StringUtils
import java.util.stream.Collectors
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtTokenFactory(private val settings: JwtSettings) {

    /**
     * Factory method for issuing new JWT Tokens.
     *
     * @param username
     * @param roles
     * @return
     */
    fun createAccessJwtToken(userContext: UserContext): AccessJwtToken {

        if (StringUtils.isBlank(userContext.username))
            throw IllegalArgumentException("Cannot create JWT Token without username")

        if (userContext.authorities == null || userContext.authorities.isEmpty())
            throw IllegalArgumentException("User doesn't have any privileges")

        val claims = Jwts.claims().setSubject(userContext.username)
        claims.put("scopes",
                userContext.authorities.stream()
                        .map { s -> s.toString() }
                        .collect(Collectors.toList()))

        val currentTime = LocalDateTime.now()

        val token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.tokenIssuer)
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime
                        .plusMinutes(settings.tokenExpirationTime!!)
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, settings.tokenSigningKey)
                .compact()

        return AccessJwtToken(token, claims)
    }

    fun createRefreshToken(userContext: UserContext): JwtToken {

        if (StringUtils.isBlank(userContext.username)) {
            throw IllegalArgumentException("Cannot create JWT Token without username")
        }

        val currentTime = LocalDateTime.now()

        val claims = Jwts.claims().setSubject(userContext.username)
        claims.put("scopes", listOf(Scopes.REFRESH_TOKEN.authority()))

        val token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(settings.tokenIssuer)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(currentTime
                        .plusMinutes(settings.refreshTokenExpTime!!)
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, settings.tokenSigningKey)
                .compact()

        return AccessJwtToken(token, claims)
    }
}