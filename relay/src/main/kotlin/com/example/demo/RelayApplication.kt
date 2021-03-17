package com.example.demo

import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2

@EnableSwagger2
@EnableRabbit
@SpringBootApplication
class RelayApplication

fun main(args: Array<String>) {
    runApplication<RelayApplication>(*args)
}
