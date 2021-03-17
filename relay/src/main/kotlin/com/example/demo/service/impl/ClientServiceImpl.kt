package com.example.demo.service.impl

import com.example.demo.exceptions.ExceptionsConstants
import com.example.demo.data.client.Client
import com.example.demo.exceptions.ExceptionsConstants.Exceptions.CPF_VALIDATION_INCORRECT
import com.example.demo.exceptions.IllegalCpfFormatException
import com.example.demo.exceptions.NoContentException
import com.example.demo.sender.SendMessage
import com.example.demo.service.ClientService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service

@Service
class ClientServiceImpl(var sendMessage: SendMessage) : ClientService {

    override fun resolveClientToShow(clients: MutableList<Client>): MutableList<Client>? {
        val clientListToReturn = mutableListOf<Client>()
        if (clients.size != 0) {
            for (item in clients.iterator()) {
                if (item.isActive == true) {
                    clientListToReturn.add(item)
                }
            }
        }

        if (clientListToReturn.size == 0)
            throw NoContentException(ExceptionsConstants.Exceptions.NO_CONTENT_EXCEPTION)
        return clientListToReturn
    }

    override fun resolveOneClientToShow(client: Client): Client {
        return if (client.isActive == true) {
            client
        } else {
            throw NoContentException(ExceptionsConstants.Exceptions.NO_CONTENT_EXCEPTION)
        }
    }

    override fun verifyCpfBeforeInsert(client: Client, method: String): Client {
        when {
            client.cpf == null -> throw IllegalCpfFormatException(CPF_VALIDATION_INCORRECT)
            else -> {
                mappingObjectToSend(client, method)
                return client
            }
        }
    }

    override fun mappingObjectToSend(client: Client, method: String) {
        val mappedObject = mutableMapOf<String, String>()
        mappedObject["method"] = method
        mappedObject["content"] = jacksonObjectMapper().writeValueAsString(client)
        return sendMessage.send(mappedObject)
    }
}