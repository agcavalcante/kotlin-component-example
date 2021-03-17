package configuration

import org.springframework.security.test.context.support.WithSecurityContext

@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory::class)
annotation class WithMockCustomUser(val fullName: String = "Gabriel Cavalcante", val username: String = "gabriel@gabriel.com.br", val password: String = "124")