package com.example.demo.controller

import com.example.demo.configuration.ExceptionsConstants
import com.example.demo.data.Component
import com.example.demo.data.ComponentRepository
import com.example.demo.exceptions.NoContentException
import com.example.demo.request.ComponentRequest
import com.example.demo.service.ComponentService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.bson.types.ObjectId
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/component")
@Api(value = "admin", description = "Rest API para processamento de estoque", tags = ["Component V1"])
class ComponentController(
    private val repository: ComponentRepository,
    private val componentService: ComponentService
) {

    @GetMapping
    @ApiOperation(value = "Exibe todos os componentes.", response = Component::class)
    fun getAllComponents(): MutableList<Component>? {
        val allReturned = repository.findAll()
            .ifEmpty { throw NoContentException(ExceptionsConstants.Exceptions.NO_CONTENT_EXCEPTION) }
        return componentService.resolveComponentToShow(allReturned)
    }

    @ApiOperation(value = "Exibe o componente por ID.", response = Component::class)
    @ApiImplicitParam(name = "id", value = "O ID procurado.", example = "6026a3c1496f7b7180f9a092", required = true)
    @GetMapping("/{id}")
    fun getOneComponent(@PathVariable("id") id: String): Component {
        val oneReturnedComponent = repository.findOneById(ObjectId(id))
        return componentService.resolveOneComponentToShow(oneReturnedComponent)
    }

    @ApiOperation(value = "Exibe componente(s) pelo nome ou parte do nome.", response = Component::class)
    @ApiImplicitParam(name = "name", value = "Nome ou parte dele.", example = "Resi", required = true)
    @GetMapping("/group/{name}")
    fun getAllComponentsWithNameContains(@PathVariable("name") name: String): MutableList<Component>? {
        val allReturned = repository.findByNameContainingIgnoreCase(name)
            .ifEmpty { throw NoContentException(ExceptionsConstants.Exceptions.NO_CONTENT_EXCEPTION) }
        return componentService.resolveComponentToShow(allReturned)
    }

    @ApiOperation(value = "Atualiza um documento já existente.", response = Component::class)
    @ApiImplicitParams(
        value = [
            ApiImplicitParam(
                name = "id",
                value = "Id do documento a ser atualizado.",
                required = true,
                example = "6026a3c1496f7b7180f9a092",
                dataType = "String"
            ),
            ApiImplicitParam(
                name = "request",
                value = "Payload que substituirá o documento na base.",
                required = true,
                dataType = "Object"
            ),
        ]
    )

    @PutMapping("/{id}")
    fun patchOneComponent(
        @RequestBody request: ComponentRequest,
        @PathVariable("id") id: String
    ): ResponseEntity<Component> {
        val componentInDB = repository.findOneById(ObjectId(id))
        val componentToUpdate = Component(
            id = componentInDB.id,
            name = request.name,
            manufacturer = request.manufacturer,
            isActive = componentInDB.isActive,
            quantity = request.quantity,
            group = request.group,
            value = request.value
        )
        return ResponseEntity.ok(componentService.verifyQuantityBeforeInsert(componentToUpdate, RequestMethod.PUT.name))
    }

    @ApiOperation(value = "Cria um novo documento na base.", response = Component::class)
    @ApiImplicitParam(name = "request", value = "Documento a ser inserido.", required = true, dataType = "Object")
    @PostMapping
    fun postOneComponent(@RequestBody request: ComponentRequest): ResponseEntity<Component> {
        val componentReceived = Component(
            name = request.name, manufacturer = request.manufacturer,
            isActive = true, quantity = request.quantity, group = request.group, value = request.value
        )
        return ResponseEntity(
            componentService.verifyQuantityBeforeInsert(componentReceived, RequestMethod.POST.name),
            HttpStatus.OK
        )
    }

    @ApiOperation(value = "Inativa um documento já existente.", response = Component::class)
    @ApiImplicitParam(
        name = "id",
        value = "Id do documento que será inativado.",
        example = "6026a3c1496f7b7180f9a092",
        required = true,
        dataType = "Object"
    )

    @DeleteMapping("/inactive/{id}")
    fun inactiveOneComponent(@PathVariable("id") id: String): ResponseEntity<Unit> {
        val componentInDB = repository.findOneById(ObjectId(id))
        componentService.mappingObjectToSend(
            Component(
                id = componentInDB.id,
                name = componentInDB.name,
                manufacturer = componentInDB.manufacturer,
                isActive = false,
                quantity = componentInDB.quantity,
                group = componentInDB.group,
                value = componentInDB.value
            ), RequestMethod.DELETE.name
        )
        return ResponseEntity.noContent().build()
    }
}