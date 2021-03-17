package com.example.consumer.data

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.CrudRepository

interface ClientRepository : MongoRepository<Client, String>, CrudRepository<Client, String> {

}