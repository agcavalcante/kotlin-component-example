package com.example.demo.security

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RestAccessDeniedHandler() : AccessDeniedHandler {

    @Throws(IOException::class, ServletException::class)
    override fun handle(
        request: HttpServletRequest?,
        httpServletResponse: HttpServletResponse?,
        e: AccessDeniedException?
    ) {
        httpServletResponse?.contentType = "application/json"
        httpServletResponse?.status = HttpServletResponse.SC_UNAUTHORIZED
        httpServletResponse?.outputStream?.println("{ \"error\": \"" + e?.message + "\" }")
    }

}