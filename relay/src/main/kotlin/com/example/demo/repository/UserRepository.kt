package com.example.demo.repository

import com.example.demo.data.user.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, String> {
    fun findByEmail(email: String?): User?

}