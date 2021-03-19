package com.example.demo.security

import com.example.demo.authorization
import com.example.demo.bearer
import com.example.demo.data.user.Credentials
import com.example.demo.data.user.UserDetailsImpl
import com.example.demo.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter: UsernamePasswordAuthenticationFilter {

    private var jwtUtil: JWTUtil

    private var userService: UserService

    constructor(authenticationManager: AuthenticationManager, jwtUtil: JWTUtil, userService: UserService) : super() {
        this.authenticationManager = authenticationManager
        this.jwtUtil = jwtUtil
        this.userService = userService
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication? {
        try {
            val (email, password) = ObjectMapper().readValue(request.inputStream, Credentials::class.java)
            userService.verifyIfUserIsActive(email)
            val token = UsernamePasswordAuthenticationToken(email, password)
            return authenticationManager.authenticate(token)
        } catch (e: Exception) {
            throw UsernameNotFoundException("User not found!")
        }
    }

    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse, chain: FilterChain?, authResult: Authentication) {
        val username = (authResult.principal as UserDetailsImpl).username
        val token = jwtUtil.generateToken(username)
        response.addHeader(authorization, "$bearer $token")
    }

}