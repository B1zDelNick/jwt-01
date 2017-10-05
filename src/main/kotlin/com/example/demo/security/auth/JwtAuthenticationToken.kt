package com.example.demo.security.auth

import org.springframework.security.core.GrantedAuthority
import com.example.demo.security.model.UserContext
import com.example.demo.security.model.token.RawAccessJwtToken
import org.springframework.security.authentication.AbstractAuthenticationToken

class JwtAuthenticationToken : AbstractAuthenticationToken {

    private var rawAccessToken: RawAccessJwtToken? = null
    private var userContext: UserContext? = null

    constructor(unsafeToken: RawAccessJwtToken) : super(null) {
        this.rawAccessToken = unsafeToken
        this.isAuthenticated = false
    }

    constructor(userContext: UserContext, authorities: Collection<GrantedAuthority>) : super(authorities) {
        this.eraseCredentials()
        this.userContext = userContext
        super.setAuthenticated(true)
    }

    override fun setAuthenticated(authenticated: Boolean) {
        if (authenticated) {
            throw IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead")
        }
        super.setAuthenticated(false)
    }

    override fun getCredentials(): Any? {
        return rawAccessToken
    }

    override fun getPrincipal(): Any? {
        return this.userContext
    }

    override fun eraseCredentials() {
        super.eraseCredentials()
        this.rawAccessToken = null
    }

    companion object {
        private val serialVersionUID = 2877954820905567501L
    }
}