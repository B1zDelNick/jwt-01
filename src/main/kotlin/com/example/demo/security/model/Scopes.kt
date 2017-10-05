package com.example.demo.security.model

enum class Scopes {
    REFRESH_TOKEN;

    fun authority(): String {
        return "ROLE_" + this.name
    }
}