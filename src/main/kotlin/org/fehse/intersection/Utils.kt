package org.fehse.intersection

import kotlin.math.absoluteValue
import kotlin.random.Random

/**
 * Generates a list of random (optionally unique) integers.
 *
 * @param size The size of the list to generate
 * @param interval The range to choose the values from
 * @param uniqueElements Boolean flag whether the generated elements are unique in the resulting
 *   list. If `true`, the interval must span at least as many elements as the chosen size.
 * @return A list of (optionally unique) integers
 * @throws IllegalArgumentException If the chosen `size < 0`.
 * @throws IllegalArgumentException If unique elements but interval smaller than the size of the
 *   list.
 */
fun randomList(size: Int, interval: IntRange, uniqueElements: Boolean = true): List<Int> {
    if (size < 0) throw IllegalArgumentException("size must be >= 0, but was $size")

    if (!uniqueElements) return List(size) { Random.nextInt(interval.first, interval.last) }

    if (size > interval.count())
        throw IllegalArgumentException(
            "Cannot choose unique elements when the final list size ($size) is larger than the possible range of values (${interval.first}..${interval.last})}"
        )

    val availableElements = interval.toMutableList()
    return List(size) {
        availableElements.removeAt(Random.nextInt().absoluteValue % availableElements.size)
    }
}
