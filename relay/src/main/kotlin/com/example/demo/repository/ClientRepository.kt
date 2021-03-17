package com.example.demo.repository

import com.example.demo.data.client.Client
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.CrudRepository

interface ClientRepository : MongoRepository<Client, String>, CrudRepository<Client, String> {

    fun findOneById(id: ObjectId): Client

    fun findByNameContainingIgnoreCase(name: String): MutableList<Client>

    override fun deleteAll()
}