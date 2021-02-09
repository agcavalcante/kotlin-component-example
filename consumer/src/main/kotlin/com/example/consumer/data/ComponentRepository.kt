package com.example.consumer.data

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.CrudRepository

interface ComponentRepository : MongoRepository<Component, String>, CrudRepository<Component, String> {

}