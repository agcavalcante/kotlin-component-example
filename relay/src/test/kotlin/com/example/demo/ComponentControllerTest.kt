package com.example.demo

import builder.ComponentBuilder
import com.example.demo.data.Component
import com.example.demo.data.ComponentRepository
import com.example.demo.request.ComponentRequest
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
    private val defaultComponentId = "507f1f77bcf86cd799439011"

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @LocalServerPort
    protected var port: Int = 0

    @BeforeEach
    fun setUp() {
        repository.deleteAll()
    }

    private fun getBaseUrl(): String = "http://localhost:$port/component"
    private fun saveOneComponent() = repository.save(
        Component(
            ObjectId(defaultComponentId).toString(),
            "BC547",
            "Siemens",
            true,
            quantity = 10,
            group = "Transistor",
            value = 0.35
        )
    )

    private fun componentRequest() = ComponentRequest(
        "BC547", "Description",
        10, "Transistor", 0.35
    )

    @Test
    fun `should return a single component by id`() {
        saveOneComponent()
        val response = restTemplate.getForEntity(getBaseUrl() + "/$defaultComponentId", Component::class.java)
        assertEquals(200, response.statusCode.value())
        assertEquals(defaultComponentId, response.body?.id)
    }

    fun saveWithIsFalseProducts() {
        val listOfComponentsToSave = mutableListOf<Component>()
        val componentTrue = ComponentBuilder.Builder()
            .id(ObjectId().toString()).name("BC328 PNP")
            .manufacturer("General Components").isActive(true)
            .quantity(100).group("Transistor").value(0.12).build()
        val componentFalse = ComponentBuilder.Builder()
            .id(ObjectId().toString()).name("BC327 PNP")
            .manufacturer("General Components").isActive(false)
            .quantity(100).group("Transistor").value(0.12).build()
        listOfComponentsToSave.add(componentTrue)
        listOfComponentsToSave.add(componentFalse)
        repository.saveAll(listOfComponentsToSave)
    }

    @Test
    fun `should return just isActive equals true`() {
        saveWithIsFalseProducts()
        val response = restTemplate.getForEntity(getBaseUrl(), Array<Component>::class.java)
        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
    }

    fun mockWithQuantityZero(): Component {
        return ComponentBuilder.Builder()
            .id(ObjectId().toString()).name("BC327 PNP")
            .manufacturer("General Components").isActive(false)
            .quantity(0).group("Transistor").value(0.12).build()
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
}

