package com.example.consumer.service

import com.example.consumer.data.Client
import com.example.consumer.data.ClientRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.springframework.stereotype.Service

@Service
class MapperService(private val mapper: ObjectMapper,
                    private val clientRepository: ClientRepository) {

    fun remappingToClient(content: String) {
        val jsonObject = Gson().fromJson(content, JsonObject::class.java)
        val componentMapped = mapper.readValue(jsonObject.get("content").asString, Client::class.java)
        when (jsonObject.get("method").asString) {
            "POST" -> clientRepository.save(componentMapped)
            "PUT" -> clientRepository.save(componentMapped)
            "DELETE" -> clientRepository.save(componentMapped)
        }
    }
}
