package com.example.demo.service

import com.example.demo.data.client.Client

interface ClientService {

    fun resolveClientToShow(clients: MutableList<Client>): MutableList<Client>?

    fun resolveOneClientToShow(client: Client): Client

    fun verifyAlreadyDeletedUser(client: Client, method: String)

    fun mappingObjectToSend(client: Client, method: String)

    fun verifyCpfBeforeInsert(client: Client, method: String): Client
}