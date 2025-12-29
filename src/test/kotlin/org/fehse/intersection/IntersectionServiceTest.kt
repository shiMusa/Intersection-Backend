package org.fehse.intersection

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class IntersectionServiceTest {

    var service = IntersectionServiceImpl()

    @Test
    fun `intersection of two lists`() {
        val listA = listOf(1, 2, 3, 4)
        val listB = listOf(2, 4, 6, 8, 10)

        val res = service.intersection(listA, listB)

        assertEquals(res, listOf(2, 4))
    }

    @Test
    fun `intersection of two lists with no intersection`() {
        val listA = listOf(1, 2, 3, 4)
        val listB = listOf(5, 6, 7, 8)

        val res = service.intersection(listA, listB)

        assertEquals(res.size, 0)
    }
}
