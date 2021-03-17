package com.example.demo

import builder.ClientBuilder
import com.example.demo.data.client.Client
import com.example.demo.repository.ClientRepository
import com.fasterxml.jackson.databind.ObjectMapper
import configuration.WithMockCustomUser
import org.bson.types.ObjectId
import org.hamcrest.Matchers
import org.hamcrest.Matchers.hasSize
import org.hamcrest.Matchers.hasValue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@SpringBootTest(
    properties = ["spring.data.mongodb.database=test"],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(SpringExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class ClientControllerTest @Autowired constructor(
    private val repository: ClientRepository
) {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        repository.deleteAll()
        saveSomeClients()
    }

    private val defaultClientId = "507f1f77bcf86cd799439011"

    private fun saveSomeClients() {
        val listOfClientsToSave = mutableListOf<Client>()
        val clientOneTrue = ClientBuilder.Builder()
            .id(defaultClientId).name("Bill Gates")
            .isActive(true).cpf("123.123.123-12").build()
        val clientTwoTrue = ClientBuilder.Builder()
            .id(ObjectId().toString()).name("Bill Cosby")
            .isActive(true).cpf("000.000.000-00").build()
        val clientThreeFalse = ClientBuilder.Builder()
            .id(ObjectId().toString()).name("Bill Clinton")
            .isActive(false).cpf("000.000.000-00").build()
        listOfClientsToSave.add(clientOneTrue)
        listOfClientsToSave.add(clientTwoTrue)
        listOfClientsToSave.add(clientThreeFalse)
        repository.saveAll(listOfClientsToSave)
    }

    private fun objectToUpdate(): Client {
        return ClientBuilder.Builder()
            .id(defaultClientId).name("Bill Murray")
            .isActive(true).cpf("123.123.123-12").build()
    }

    @Test
    @WithMockCustomUser
    fun `should return a single client by id`() {
        this.mockMvc.perform(
            MockMvcRequestBuilders
                .get("/client/$defaultClientId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().`is`(200))
            .andExpect(jsonPath("$.id").value(defaultClientId))
    }

    @Test
    @WithMockCustomUser
    fun `should return just isActive equals true`() {
        this.mockMvc.perform(
            MockMvcRequestBuilders
                .get("/client")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().`is`(200))
            .andExpect(jsonPath("$.*", hasSize<Int>(2)))
    }

    @Test
    @WithMockCustomUser
    fun `should return a client by part of its name`() {
        this.mockMvc.perform(
            MockMvcRequestBuilders
                .get("/client/search/{name}", "Bil")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().`is`(200))
    }

    @Test
    @WithMockCustomUser
    fun `should put a object client with success`() {
        this.mockMvc.perform(
            MockMvcRequestBuilders
                .put("/client/{id}", defaultClientId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(objectToUpdate()))
        )
            .andExpect(content().contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().`is`(200))
            .andExpect(jsonPath("$.cpf").value("123.123.123-12"))
    }

    @Test
    @WithMockCustomUser
    fun `should inactive one document`() {
        this.mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/client/inactive/{id}", defaultClientId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().`is`(204))
    }
}
