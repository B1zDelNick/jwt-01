package com.example.demo.web

import com.example.demo.security.auth.JwtAuthenticationToken
import com.example.demo.security.model.UserContext
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ApiEndpoint {

    @GetMapping("/me")
    fun test1(token: JwtAuthenticationToken) = ResponseEntity.ok(token.principal as UserContext)

}