package com.example.demo.security

import com.example.demo.security.model.entities.User
import java.util.*

interface UserService {
    fun getByUsername(username: String): Optional<User>
}