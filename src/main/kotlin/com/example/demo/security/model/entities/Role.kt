package com.example.demo.security.model.entities

enum class Role {
    ADMIN, PREMIUM_MEMBER, MEMBER;

    fun authority(): String {
        return "ROLE_" + this.name
    }
}