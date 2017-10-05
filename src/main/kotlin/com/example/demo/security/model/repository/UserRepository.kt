package com.example.demo.security.model.repository

import com.example.demo.security.model.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<User, Long> {
    fun findByUsername(username: String): Optional<User>
}