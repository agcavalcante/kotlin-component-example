package com.example.demo.data.user

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class User(
        @Id
        val id: String? = ObjectId.get().toString(),
        val fullName: String = "",
        val email: String = "",
        var password: String = ""
)