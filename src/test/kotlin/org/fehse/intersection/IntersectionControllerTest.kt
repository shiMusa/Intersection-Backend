package org.fehse.intersection

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import tools.jackson.databind.ObjectMapper

@WebMvcTest
class IntersectionControllerTest {

    @Autowired lateinit var mockMvc: MockMvc

    @MockitoBean lateinit var service: IntersectionService

    private val objectMapper = ObjectMapper()

    @Test
    fun `POST on benchmark return message`() {
        val requestBody = BenchmarkInput(10, 1000, 100)
        val requestJson = objectMapper.writeValueAsString(requestBody)
        println("requestJson: $requestJson")

        mockMvc
            .post("/intersection/benchmark") {
                contentType = MediaType.APPLICATION_JSON
                content = requestJson
            }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$[\"List sizes\"]").value(Pair(10, 1000))
                jsonPath("$[\"Average time (ns) of small list to set\"]").exists()
                jsonPath("$[\"Average time (ns) of large list to set\"]").exists()
            }
    }

    @Test
    fun `POST on calculate returns correct message format`() {
        val listSizeA = 4
        val listSizeB = 8
        val requestBody = ExecutionInput(listSizeA, listSizeB, true)

        val requestJson = objectMapper.writeValueAsString(requestBody)
        println("requestJson: $requestJson")

        mockMvc
            .post("/intersection/calculate") {
                contentType = MediaType.APPLICATION_JSON
                content = requestJson
            }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$[\"time in ms\"]").exists()
            }
    }
}
