package com.example.demo.security.model

import org.apache.commons.lang3.StringUtils
import org.springframework.security.core.GrantedAuthority


class UserContext private constructor(
        val username: String,
        val authorities: List<GrantedAuthority>) {

    companion object {

        fun create(username: String?, authorities: List<GrantedAuthority>): UserContext {
            if (StringUtils.isBlank(username))
                throw IllegalArgumentException("Username is blank: " + username)
            return UserContext(username!!, authorities)
        }
    }
}