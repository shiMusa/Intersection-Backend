package org.fehse.intersection

import org.springframework.stereotype.Service

/**
 * A service for calculating the intersection of two lists of integers.
 *
 * This interface provides methods for calculating intersection where the user can choose which of
 * the two lists will be turned into a set, while the other list will be iterated over.
 */
interface IntersectionService {
    /**
     * Calculates the intersection of two lists of integers.
     *
     * Returns a new list of integers that are contained in both input lists. In this process, the
     * first input list `a` will be turned into a `Set`.
     *
     * **Important:** The input lists must not contain duplicate entries!
     *
     * @param listToSet The first list of unique integers - will be turned into a `Set`.
     * @param listIterated The second list of unique integers - will be iterated over.
     * @return A list of integers found in both input lists
     */
    fun intersection(listToSet: List<Int>, listIterated: List<Int>): List<Int>

    /**
     * Calculates the intersection of two lists of integers.
     *
     * Returns a new list of integers that are contained in both input lists. In this process,
     * either the smaller or larger of the two lists will be turned in to a set, depending on the
     * flag `smallerToSet`.
     *
     * **Important:** The input lists must not contain duplicate entries!
     *
     * @param listA The first list of unique integers.
     * @param listB The second list of unique integers.
     * @param smallerToSet Boolean flag determining whether the smaller or larger of the two lists
     *   will be turned into a set, while the other list will be iterated over.
     * @return A list of integers found in both input lists.
     */
    fun intersectionBySize(
        listA: List<Int>,
        listB: List<Int>,
        smallerToSet: Boolean = true,
    ): List<Int>
}

@Service
class IntersectionServiceImpl : IntersectionService {

    override fun intersectionBySize(
        listA: List<Int>,
        listB: List<Int>,
        smallerToSet: Boolean,
    ): List<Int> {
        val (smaller, larger) =
            if (listA.size < listB.size) Pair(listA, listB) else Pair(listB, listA)
        val (left, right) = if (smallerToSet) Pair(smaller, larger) else Pair(larger, smaller)
        return intersection(left, right)
    }

    override fun intersection(listToSet: List<Int>, listIterated: List<Int>): List<Int> {
        val set = listToSet.toSet()
        return listIterated.filter { it in set }
    }
}
