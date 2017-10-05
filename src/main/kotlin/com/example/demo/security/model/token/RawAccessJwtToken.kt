package com.example.demo.security.model.token

import com.example.demo.security.error.exceptions.JwtExpiredTokenException
import io.jsonwebtoken.*
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException


class RawAccessJwtToken(override val token: String) : JwtToken {

    /**
     * Parses and validates JWT Token signature.
     *
     * @throws BadCredentialsException
     * @throws JwtExpiredTokenException
     */
    fun parseClaims(signingKey: String): Jws<Claims> = try {
            Jwts.parser().setSigningKey(signingKey).parseClaimsJws(this.token)
        } catch (ex: UnsupportedJwtException) {
            logger.error("Invalid JWT Token", ex)
            throw BadCredentialsException("Invalid JWT token: ", ex)
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT Token", ex)
            throw BadCredentialsException("Invalid JWT token: ", ex)
        } catch (ex: IllegalArgumentException) {
            logger.error("Invalid JWT Token", ex)
            throw BadCredentialsException("Invalid JWT token: ", ex)
        } catch (ex: SignatureException) {
            logger.error("Invalid JWT Token", ex)
            throw BadCredentialsException("Invalid JWT token: ", ex)
        } catch (expiredEx: ExpiredJwtException) {
            logger.info("JWT Token is expired", expiredEx)
            throw JwtExpiredTokenException(this, "JWT Token expired", expiredEx)
        }

    companion object {
        private val logger = LoggerFactory.getLogger(RawAccessJwtToken::class.java)
    }
}