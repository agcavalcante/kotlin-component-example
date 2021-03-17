package com.example.demo.service

import com.example.demo.exceptions.ExceptionsConstants
import com.example.demo.exceptions.ExceptionsConstants.Exceptions.QUANTITY_EQUALS_OR_LOWER_THAN_ZERO
import com.example.demo.data.component.Component
import com.example.demo.exceptions.IllegalQuantityException
import com.example.demo.exceptions.NoContentException
import com.example.demo.sender.SendMessage
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service

@Service
class ComponentServiceImpl(var sendMessage: SendMessage) : ComponentService{

    override fun resolveComponentToShow(components: MutableList<Component>): MutableList<Component>? {
        val componentListToReturn = mutableListOf<Component>()
        if (components.size != 0) {
            for (item in components.iterator()) {
                if (item.isActive == true) {
                    componentListToReturn.add(item)
                }
            }
        }

        if (componentListToReturn.size == 0)
            throw NoContentException(ExceptionsConstants.Exceptions.NO_CONTENT_EXCEPTION)
        return componentListToReturn
    }

    override fun resolveOneComponentToShow(component: Component): Component {
        return if (component.isActive == true) {
            component
        } else {
            throw NoContentException(ExceptionsConstants.Exceptions.NO_CONTENT_EXCEPTION)
        }
    }

    override fun verifyQuantityBeforeInsert(component: Component, method: String): Component {
        when {
            component.quantity == 0 -> throw IllegalQuantityException(QUANTITY_EQUALS_OR_LOWER_THAN_ZERO)
            component.quantity!! < 0 -> throw IllegalQuantityException(QUANTITY_EQUALS_OR_LOWER_THAN_ZERO)
            else -> {
                mappingObjectToSend(component, method)
                return component
            }
        }
    }

    override fun mappingObjectToSend(component: Component, method: String) {
        val mappedObject = mutableMapOf<String, String>()
        mappedObject["method"] = method
        mappedObject["content"] = jacksonObjectMapper().writeValueAsString(component)
        return sendMessage.send(mappedObject)
    }
}