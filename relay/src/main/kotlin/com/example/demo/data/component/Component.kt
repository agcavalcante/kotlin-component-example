package com.example.demo.data.component

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Component(
    @Id
    val id: String? = ObjectId.get().toString(),
    val name: String?,
    val manufacturer: String?,
    val isActive: Boolean?,
    val quantity: Int?,
    val group: String?,
    val value: Double?
)