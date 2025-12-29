package org.fehse.intersection

import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import tools.jackson.databind.ObjectMapper


@WebMvcTest(IntersectionController::class)
class IntersectionControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    lateinit var service: IntersectionService

    @Test
    fun `POST on benchmark returns BenchmarkOutput json`() {
        // IMPORTANT: stub both variants used by the controller (default + explicit smallerToSet=false)
        whenever(service.intersectionBySize(any(), any(), any())).thenReturn(emptyList())

        val requestBody = BenchmarkInput(listSizeA = 10, listSizeB = 1000, iterations = 2)
        val requestJson = objectMapper.writeValueAsString(requestBody)

        mockMvc.post("/intersection/benchmark") {
            contentType = MediaType.APPLICATION_JSON
            content = requestJson
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }

            jsonPath("$.listSizeA").value(10)
            jsonPath("$.listSizeB").value(1000)

            jsonPath("$.meanMsLarge").isNumber
            jsonPath("$.meanMsSmall").isNumber
            jsonPath("$.meanErrMsLarge").isNumber
            jsonPath("$.meanErrMsSmall").isNumber
        }
    }

    @Test
    fun `POST on calculate returns ExecutionOutput json`() {
        whenever(service.intersection(any(), any())).thenReturn(listOf(1, 2, 3))

        val requestBody = ExecutionInput(listSizeA = 4, listSizeB = 8, listAToSet = true)
        val requestJson = objectMapper.writeValueAsString(requestBody)

        mockMvc.post("/intersection/calculate") {
            contentType = MediaType.APPLICATION_JSON
            content = requestJson
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }

            jsonPath("$.timeMs").isNumber
            jsonPath("$.listSize").value(3)
        }
    }
}
