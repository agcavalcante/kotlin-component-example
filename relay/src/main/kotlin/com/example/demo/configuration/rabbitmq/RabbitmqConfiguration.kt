package com.example.demo.configuration.rabbitmq

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitmqConfiguration(
    @Value("\${spring.rabbit.url}") private val URL: String,
    @Value("\${spring.rabbit.queue}") private val QUEUE_NAME: String
) {

    fun conn(): Connection? {
        val factory = ConnectionFactory()
        println(URL)
        return factory.newConnection(URL)
    }

    @Bean
    fun declareQueue() {
        conn()?.createChannel()
            ?.queueDeclare(QUEUE_NAME, true, false, false, null)
    }

    @Bean
    fun declareExchange() {
        conn()?.createChannel()?.exchangeDeclare("component-exchange", "direct", true)
    }

    @Bean
    fun declareQueueBind() {
        conn()?.createChannel()?.queueBind(QUEUE_NAME, "component-exchange", "")
    }

    fun send(message: MutableMap<String, String>) {
        conn().use { connection ->
            connection?.createChannel()?.use { channel ->
                channel.basicPublish(
                    "component-exchange", "", null,
                    jacksonObjectMapper().writeValueAsBytes(message)
                )
                //println("[x] Sent '$message")
            }
        }
    }
}