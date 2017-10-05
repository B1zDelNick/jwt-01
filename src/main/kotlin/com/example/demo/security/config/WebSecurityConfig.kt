package com.example.demo.security.config

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.authentication.AuthenticationManager
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import com.example.demo.security.RestAuthenticationEntryPoint
import com.example.demo.security.auth.jwt.JwtAuthenticationProvider
import com.example.demo.security.auth.ajax.AjaxAuthenticationProvider
import com.example.demo.security.auth.ajax.AjaxLoginProcessingFilter
import com.example.demo.security.auth.filters.CustomCorsFilter
import com.example.demo.security.auth.jwt.JwtTokenAuthenticationProcessingFilter
import com.example.demo.security.auth.jwt.SkipPathRequestMatcher
import com.example.demo.security.auth.jwt.extractor.TokenExtractor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity


@Configuration
@EnableWebSecurity
class WebSecurityConfig(
        private val authenticationEntryPoint: RestAuthenticationEntryPoint,
        private val successHandler: AuthenticationSuccessHandler,
        private val failureHandler: AuthenticationFailureHandler,
        private val ajaxAuthenticationProvider: AjaxAuthenticationProvider,
        private val jwtAuthenticationProvider: JwtAuthenticationProvider,
        private val tokenExtractor: TokenExtractor,
        private val authenticationManager: AuthenticationManager,
        private val objectMapper: ObjectMapper) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    protected fun buildAjaxLoginProcessingFilter(): AjaxLoginProcessingFilter {
        val filter = AjaxLoginProcessingFilter(FORM_BASED_LOGIN_ENTRY_POINT, successHandler, failureHandler, objectMapper)
        filter.setAuthenticationManager(this.authenticationManager)
        return filter
    }

    @Throws(Exception::class)
    protected fun buildJwtTokenAuthenticationProcessingFilter(): JwtTokenAuthenticationProcessingFilter {
        val pathsToSkip = listOf(TOKEN_REFRESH_ENTRY_POINT, FORM_BASED_LOGIN_ENTRY_POINT)
        val matcher = SkipPathRequestMatcher(pathsToSkip, TOKEN_BASED_AUTH_ENTRY_POINT)
        val filter = JwtTokenAuthenticationProcessingFilter(failureHandler, tokenExtractor, matcher)
        filter.setAuthenticationManager(this.authenticationManager)
        return filter
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(ajaxAuthenticationProvider)
        auth.authenticationProvider(jwtAuthenticationProvider)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
            http
                // We don't need CSRF for JWT based authentication
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(this.authenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                    .antMatchers(FORM_BASED_LOGIN_ENTRY_POINT).permitAll() // Login end-point
                    .antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll() // Token refresh end-point
                    .antMatchers("/console").permitAll() // H2 Console Dash-board - only for testing
                    .and()
                .authorizeRequests()
                    .antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated() // Protected API End-points
                    .and()
                .addFilterBefore(CustomCorsFilter(), UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(buildAjaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    companion object {
        val JWT_TOKEN_HEADER_PARAM = "X-Authorization"
        val FORM_BASED_LOGIN_ENTRY_POINT = "/api/auth/login"
        val TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**"
        val TOKEN_REFRESH_ENTRY_POINT = "/api/auth/token"
    }
}