package com.example.consumer.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Client(
    @Id
    val id: String? = ObjectId.get().toString(),
    val name: String?,
    val isActive: Boolean?,
    val cpf: String?
) {
}