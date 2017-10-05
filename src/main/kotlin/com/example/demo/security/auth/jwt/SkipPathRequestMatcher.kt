package com.example.demo.security.auth.jwt

import io.jsonwebtoken.lang.Assert
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
//import org.springframework.util.Assert
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest


class SkipPathRequestMatcher(pathsToSkip: List<String>, processingPath: String) : RequestMatcher {

    private val matchers: OrRequestMatcher
    private val processingMatcher: RequestMatcher

    init {
        Assert.notNull(pathsToSkip)

        val m: List<RequestMatcher> = pathsToSkip.stream()
                .map { path -> AntPathRequestMatcher(path) }
                .collect(Collectors.toList())

        matchers = OrRequestMatcher(m)
        processingMatcher = AntPathRequestMatcher(processingPath)
    }

    override fun matches(request: HttpServletRequest): Boolean {
        if (matchers.matches(request)) {
            return false
        }
        return processingMatcher.matches(request)
    }
}