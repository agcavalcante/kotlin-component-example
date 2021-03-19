package com.example.demo.security

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.ServletException
import java.io.IOException
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest

@Component
class RestAuthenticationEntryPoint : AuthenticationEntryPoint{

    @Throws(IOException::class, ServletException::class)
    override fun commence(
        httpServletRequest: HttpServletRequest?,
        httpServletResponse: HttpServletResponse,
        e: AuthenticationException?
    ) {
        httpServletResponse.contentType = "application/json"
        httpServletResponse.status = HttpServletResponse.SC_UNAUTHORIZED
        httpServletResponse.outputStream.println("{ \"error\": \"" + e?.message + "\" }")
    }
}