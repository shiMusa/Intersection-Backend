package org.fehse.intersection

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UtilsTest {

    @Test
    fun `generate random list`() {
        val listSize = 125
        val interval = -100 until 100
        val list = randomList(listSize, interval)
        assertEquals(list.size, listSize)
        assert(list.max() <= interval.last)
        assert(list.min() >= interval.first)
        assertEquals(list.distinct().size, listSize) // no duplicates
    }

    @Test
    fun `check for invalid input size`() {
        val exception = assertThrows<IllegalArgumentException> { randomList(-1, 0..10) }
        assertEquals(exception.message, "size must be >= 0, but was -1")
    }

    @Test
    fun `check for invalid input range and unique elements`() {
        val exception = assertThrows<IllegalArgumentException> { randomList(100, 0 until 10) }
        assertEquals(
            exception.message,
            "Cannot choose unique elements when the final list size (100) is larger than the possible range of values (0..9)}",
        )
    }

    @Test
    fun `check for valid input range and no unique elements`() {
        val res = randomList(100, 0 until 10, uniqueElements = false)
        assertEquals(res.size, 100)
    }
}
