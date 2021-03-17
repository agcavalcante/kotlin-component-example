package com.example.demo.data.client

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Client(
    @Id
    val id: String? = ObjectId.get().toString(),
    val name: String?,
    val isActive: Boolean?,
    val cpf: String?
)