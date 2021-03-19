package com.example.demo.data.user

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Document
class User(
        @Id
        val id: String? = ObjectId.get().toString(),
        val fullName: String,
        val email: String,
        val birthDate: LocalDate,
        var password: String,
        val isActive: Boolean = false
)