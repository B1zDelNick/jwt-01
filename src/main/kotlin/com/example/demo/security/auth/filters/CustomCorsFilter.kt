package com.example.demo.security.auth.filters

import org.springframework.stereotype.Component
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.filter.CorsFilter

//@Component TODO mb bean needed
class CustomCorsFilter : CorsFilter(configurationSource()) {

    companion object {
        private fun configurationSource(): UrlBasedCorsConfigurationSource {
            val config = CorsConfiguration()
            config.allowCredentials = true
            config.addAllowedOrigin("*")
            config.addAllowedHeader("*")
            config.maxAge = 36000L
            config.allowedMethods = listOf("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
            val source = UrlBasedCorsConfigurationSource()
            source.registerCorsConfiguration("/api/**", config) // TODO path configure
            return source
        }
    }
}