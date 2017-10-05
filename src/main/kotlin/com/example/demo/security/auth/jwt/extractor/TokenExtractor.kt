package com.example.demo.security.auth.jwt.extractor

interface TokenExtractor {
    fun extract(payload: String): String
}