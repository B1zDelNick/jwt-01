package com.example.demo.security.auth.ajax

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore


class LoginRequest
    @JsonCreator constructor(
            @param:JsonProperty("username") val username: String,
            @JsonIgnore @param:JsonProperty("password") val password: String)