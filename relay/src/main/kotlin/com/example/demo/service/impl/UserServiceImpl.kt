package com.example.demo.service.impl

import com.example.demo.data.user.User
import com.example.demo.data.user.UserDetailsImpl
import com.example.demo.exceptions.ExceptionsConstants.Exceptions.ACCOUNT_NOT_ACTIVATED
import com.example.demo.exceptions.NotActivatedAccountException
import com.example.demo.repository.UserRepository
import com.example.demo.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    override fun create(user: User): User {
        user.password = bCryptPasswordEncoder.encode(user.password)
        return userRepository.save(user)
    }

    override fun verifyIfUserIsActive(email: String?) {
        if(userRepository.findByEmail(email)?.isActive == false) {
            throw NotActivatedAccountException(ACCOUNT_NOT_ACTIVATED)
        }
    }
}
