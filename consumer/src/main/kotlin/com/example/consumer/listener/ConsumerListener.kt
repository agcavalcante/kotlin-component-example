package com.example.consumer.listener

import com.example.consumer.service.MapperService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class ConsumerListener(val mapperService: MapperService) {

    @RabbitListener(queues = ["\${spring.rabbit.queue}"])
    fun receivedMessage(msg: String) {
        mapperService.remappingToComponent(msg)
    }

}
