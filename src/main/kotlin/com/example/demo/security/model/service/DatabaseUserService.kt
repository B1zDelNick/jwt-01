package com.example.demo.security.model.service

import com.example.demo.security.model.repository.UserRepository
import com.example.demo.security.UserService
import com.example.demo.security.model.entities.User
import org.springframework.stereotype.Service
import java.util.*


@Service
class DatabaseUserService(
        val userRepository: UserRepository) : UserService {

    override fun getByUsername(username: String): Optional<User> {
        return this.userRepository.findByUsername(username)
    }
}