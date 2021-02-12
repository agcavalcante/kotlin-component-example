package com.example.demo

import builder.ComponentBuilder
import com.example.demo.data.Component
import com.example.demo.data.ComponentRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.bson.types.ObjectId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@SpringBootTest(
    properties = ["spring.data.mongodb.database=mongo-test"],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class ComponentControllerTest @Autowired constructor(
    private val repository: ComponentRepository,
    private val restTemplate: TestRestTemplate
) {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @LocalServerPort
    protected var port: Int = 0

    @BeforeEach
    fun setUp() {
        repository.deleteAll()
        saveOneComponents()
    }

    private val defaultComponentId = "507f1f77bcf86cd799439011"
    private fun getBaseUrl(): String = "http://localhost:$port/component"
    private fun saveOneComponents() {
        val listOfComponentsToSave = mutableListOf<Component>()
        val componentTrue1 = ComponentBuilder.Builder()
            .id(ObjectId().toString()).name("BC328 PNP")
            .manufacturer("General Components").isActive(true)
            .quantity(100).group("Transistor").value(0.12).build()
        val componentTrue2 = ComponentBuilder.Builder()
            .id(ObjectId(defaultComponentId).toString()).name("BC547 PNP")
            .manufacturer("General Components").isActive(true)
            .quantity(100).group("Transistor").value(0.35).build()
        val componentFalse = ComponentBuilder.Builder()
            .id(ObjectId().toString()).name("BC327 PNP")
            .manufacturer("General Components").isActive(false)
            .quantity(100).group("Transistor").value(0.12).build()
        listOfComponentsToSave.add(componentTrue1)
        listOfComponentsToSave.add(componentTrue2)
        listOfComponentsToSave.add(componentFalse)
        repository.saveAll(listOfComponentsToSave)
    }

    private fun mockWithQuantityZero(): Component {
        return ComponentBuilder.Builder()
            .id(ObjectId().toString()).name("BC338 PNP")
            .manufacturer("General Components").isActive(false)
            .quantity(0).group("Transistor").value(0.12).build()
    }

    private fun objectToUpdate(): Component {
        return ComponentBuilder.Builder()
            .id(ObjectId(defaultComponentId).toString()).name("BC547")
            .manufacturer("Siemens").isActive(true)
            .quantity(100).group("Transistor").value(0.12).build()
    }
//    private fun saveOneComponent() = repository.save(
//        Component(
//            ObjectId(defaultComponentId).toString(),
//            "BC547",
//            "Siemens",
//            true,
//            quantity = 10,
//            group = "Transistor",
//            value = 0.35
//        )
//    )

    //    private fun componentRequest() = ComponentRequest(
//        "BC547", "Description",
//        10, "Transistor", 0.35
//    )
    @Test
    fun `should return a single component by id`() {
        val response = restTemplate.getForEntity(getBaseUrl() + "/$defaultComponentId", Component::class.java)
        assertEquals(200, response.statusCode.value())
        assertEquals(defaultComponentId, response.body?.id)
    }

    @Test
    fun `should return just isActive equals true`() {
        val response = restTemplate.getForEntity(getBaseUrl(), Array<Component>::class.java)
        assertEquals(200, response.statusCode.value())
        assertEquals(2, response.body?.size)
    }

    @Test
    fun `cannot post with quantity equals zero`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/component")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(mockWithQuantityZero()))
        )
            .andExpect(MockMvcResultMatchers.status().`is`(406))
            .andExpect(jsonPath("$.message").value("Wrong value to parameter"))
            .andReturn()
    }

    @Test
    fun `should return a component by part of its name`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/component/group/{name}", "BC54")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().`is`(200))
    }

    @Test
    fun `should put a object with success`() {
        /*val result: MvcResult = */mockMvc.perform(
            MockMvcRequestBuilders
                .put("/component/{id}", ObjectId(defaultComponentId).toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(objectToUpdate()))
        )
            .andExpect(MockMvcResultMatchers.status().`is`(200))
            .andExpect(jsonPath("$.value").value(0.12))
            .andReturn()
    }

    @Test
    fun `should inactive one document`() {
        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/component/inactive/{id}", ObjectId(defaultComponentId).toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().`is`(204))
    }
}

