package configuration

import com.example.demo.data.user.User
import com.example.demo.data.user.UserDetailsImpl
import org.springframework.security.test.context.support.WithSecurityContextFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
class WithMockCustomUserSecurityContextFactory: WithSecurityContextFactory<WithMockCustomUser> {

    override fun createSecurityContext(customUser: WithMockCustomUser): SecurityContext? {
        val context: SecurityContext = SecurityContextHolder.createEmptyContext()
        val user = User(fullName = "Gabriel Cavalcante", email = "gabriel@gabriel.com.br", password = "124")
        val principal = UserDetailsImpl(user)
        val auth: Authentication =
            UsernamePasswordAuthenticationToken(principal, "password", principal.authorities)
        context.authentication = auth
        return context
    }
}