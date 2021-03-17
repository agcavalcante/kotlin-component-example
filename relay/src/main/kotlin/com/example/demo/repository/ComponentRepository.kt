package com.example.demo.repository

import com.example.demo.data.component.Component
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.CrudRepository

interface ComponentRepository : MongoRepository<Component, String>, CrudRepository<Component, String> {
    fun findOneById(id: ObjectId): Component
    fun findByNameContainingIgnoreCase(name: String): MutableList<Component>
    override fun deleteAll()
}