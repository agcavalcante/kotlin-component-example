package com.example.demo.controller

import com.example.demo.exceptions.ExceptionsConstants
import com.example.demo.data.client.Client
import com.example.demo.repository.ClientRepository
import com.example.demo.exceptions.NoContentException
import com.example.demo.request.ClientRequest
import com.example.demo.service.ClientService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/client")
@Api(value = "admin", description = "Rest API", tags = ["Client V1"])
class ClientController(
    private val repository: ClientRepository,
    private val clientService: ClientService
) {

    @GetMapping
    @ApiOperation(value = "Retrieve all clients.", response = Client::class)
    fun getAllClients(): MutableList<Client>? {
        val allReturned = repository.findAll()
            .ifEmpty { throw NoContentException(ExceptionsConstants.Exceptions.NO_CONTENT_EXCEPTION) }
        return clientService.resolveClientToShow(allReturned)
    }

    @ApiOperation(value = "Retrieve a client by ID.", response = Client::class)
    @ApiImplicitParam(name = "id", value = "Searched ID.", example = "6026a3c1496f7b7180f9a092", required = true)
    @GetMapping("/{id}")
    fun getClientById(@PathVariable("id") id: String): Client {
        val returnedClient = repository.findOneById(ObjectId(id))
        return clientService.resolveOneClientToShow(returnedClient)
    }

    @ApiOperation(value = "Show clients by part of name.", response = Client::class)
    @ApiImplicitParam(name = "name", value = "Name or part of it.", example = "Gabr", required = true)
    @GetMapping("/search/{name}")
    fun getAllClientsWithNameContains(@PathVariable("name") name: String): MutableList<Client>? {
        val allReturned = repository.findByNameContainingIgnoreCase(name)
            .ifEmpty { throw NoContentException(ExceptionsConstants.Exceptions.NO_CONTENT_EXCEPTION) }
        return clientService.resolveClientToShow(allReturned)
    }

    @ApiOperation(value = "Update an already created client.", response = Client::class)
    @ApiImplicitParams(
        value = [
            ApiImplicitParam(
                name = "id",
                value = "Documents' id.",
                required = true,
                example = "6026a3c1496f7b7180f9a092",
                dataType = "String"
            ),
            ApiImplicitParam(
                name = "request",
                value = "Updated data.",
                required = true,
                dataType = "Object"
            ),
        ]
    )
    @PutMapping("/{id}")
    fun putOneClient(
        @RequestBody request: ClientRequest,
        @PathVariable("id") id: String
    ): ResponseEntity<Client> {
        val clientInDB = repository.findOneById(ObjectId(id))
        val clientToUpdate = Client(
            id = clientInDB.id,
            name = request.name,
            isActive = clientInDB.isActive,
            cpf = request.cpf
        )
        return ResponseEntity.ok().body(clientService.verifyCpfBeforeInsert(clientToUpdate, RequestMethod.POST.name))
    }

    @ApiOperation(value = "Create a new client document.", response = Client::class)
    @ApiImplicitParam(name = "request", value = "Document to be inserted.", required = true, dataType = "Object")
    @PostMapping
    fun postOneClient(@RequestBody request: ClientRequest): ResponseEntity<Client> {
        val clientReceived = Client(
            name = request.name, isActive = true, cpf = request.cpf
        )
        return ResponseEntity.ok().body(clientService.verifyCpfBeforeInsert(clientReceived, RequestMethod.POST.name))
    }

    @ApiOperation(value = "Inactive an already created document.", response = Client::class)
    @ApiImplicitParam(
        name = "id",
        value = "Document ID to be inactivated.",
        example = "6026a3c1496f7b7180f9a092",
        required = true,
        dataType = "Object"
    )
    @DeleteMapping("/inactive/{id}")
    fun inactiveOneClient(@PathVariable("id") id: String): ResponseEntity<Unit> {
        val clientInDB = repository.findOneById(ObjectId(id))
        clientService.verifyAlreadyDeletedUser(clientInDB, RequestMethod.DELETE.name)
        return ResponseEntity.noContent().build()
    }
}