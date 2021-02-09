package com.example.demo.configuration.rabbitmq

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitmqConfiguration(
    @Value("\${spring.rabbitmq.username}") private val USERNAME: String,
    @Value("\${spring.rabbitmq.password}") private val PASSWORD: String,
    @Value("\${spring.rabbitmq.host}") private val HOST: String,
    @Value("\${spring.rabbitmq.port}") private val PORT: String,
    @Value("\${spring.rabbitmq.queue}") private val QUEUE_NAME: String
) {



    fun conn(): Connection? {
        val url = "amqp://${USERNAME}:${PASSWORD}@${HOST}:${PORT}/"
        val factory = ConnectionFactory()
        println(url)
        return factory.newConnection(url)
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