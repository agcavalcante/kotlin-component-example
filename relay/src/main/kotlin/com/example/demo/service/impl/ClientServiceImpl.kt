package com.example.demo.service.impl

import com.example.demo.configuration.rabbitmq.isCpf
import com.example.demo.data.client.Client
import com.example.demo.exceptions.*
import com.example.demo.exceptions.ExceptionsConstants.Exceptions.CPF_VALIDATION_INCORRECT
import com.example.demo.exceptions.ExceptionsConstants.Exceptions.USER_ALREADY_INACTIVATED
import com.example.demo.repository.ClientRepository
import com.example.demo.sender.SendMessage
import com.example.demo.service.ClientService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import java.util.*

@Service
class ClientServiceImpl(var sendMessage: SendMessage, var clientRepository: ClientRepository) : ClientService {

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

        val clientRetrieved: Optional<Client>? = client.cpf?.let { clientRepository.findOneByCpf(it) }

        if (client.cpf?.isCpf() == true) {
            if (clientRetrieved?.isPresent == false) {
                mappingObjectToSend(client, method)
                return client
            } else {
                throw UserAlreadyRegisteredException(CPF_VALIDATION_INCORRECT)
            }
        }
        throw IllegalCpfFormatException(CPF_VALIDATION_INCORRECT)
    }

    override fun verifyAlreadyDeletedUser(client: Client, method: String) {
        if (client.isActive == true) {
            return mappingObjectToSend(
                Client(
                    id = client.id,
                    name = client.name,
                    isActive = false,
                    cpf = client.cpf
                ), method
            )
        }
        throw UserAlreadyInactivatedException(USER_ALREADY_INACTIVATED)
    }

    override fun mappingObjectToSend(client: Client, method: String) {
        val mappedObject = mutableMapOf<String, String>()
        mappedObject["method"] = method
        mappedObject["content"] = jacksonObjectMapper().writeValueAsString(client)
        return sendMessage.send(mappedObject)
    }
}