package com.example.demo.service

import com.example.demo.data.user.User

interface UserService {

    fun create(user: User): User

    fun myself(): String?

}