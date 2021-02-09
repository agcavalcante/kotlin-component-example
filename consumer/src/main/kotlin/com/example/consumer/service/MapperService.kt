package com.example.consumer.service

import com.example.consumer.data.Component
import com.example.consumer.data.ComponentRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.springframework.stereotype.Service

@Service
class MapperService(private val mapper: ObjectMapper,
                    private val componentRepository: ComponentRepository) {

    fun remappingToComponent(content: String) {
        val jsonObject = Gson().fromJson(content, JsonObject::class.java)
        val componentMapped = mapper.readValue(jsonObject.get("content").asString, Component::class.java)
        when (jsonObject.get("method").asString) {
            "POST" -> componentRepository.save(componentMapped)
            "PUT" -> componentRepository.save(componentMapped)
        }
    }
}
