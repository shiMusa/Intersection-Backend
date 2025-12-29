package org.fehse.intersection

import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.system.measureNanoTime

data class BenchmarkInput(
    @field:Min(value = 1, message = "List must have at least 1 element.")
    val listSizeA: Int,
    @field:Min(value = 1, message = "List must have at least 1 element.")
    val listSizeB: Int,
    @field:Min(value = 2, message = "A benchmark must have at lest 2 elements.")
    val iterations: Int
)

data class BenchmarkOutput(
    val listSizeA: Int,
    val listSizeB: Int,
    val meanMsSmall: Double,
    val meanMsLarge: Double,
    val meanErrMsSmall: Double,
    val meanErrMsLarge: Double,
)

data class ExecutionInput(
    @field:Min(value = 1, message = "List must have at least 1 element.")
    val listSizeA: Int,
    @field:Min(value = 1, message = "List must have at least 1 element.")
    val listSizeB: Int,
    val listAToSet: Boolean
)

data class ExecutionOutput(val timeMs: Double)

/**
 * Controller for calculating and benchmarking intersections of lists of integers.
 *
 * This controller offers _POST_ endpoints for calculating the intersection of two lists of integers
 * and for benchmarking the performance depending on which of the two lists is transformed into a
 * set while the other is iterated over.
 */
@RestController
@RequestMapping("/intersection")
class IntersectionController(private val service: IntersectionService) {

    /**
     * Benchmarks the intersection calculation for both cases, either converting the smaller or the
     * larger of the two lists into a set.
     *
     * @param input The input defining the benchmark settings, consisting of the list sizes and
     *   number of iterations.
     * @return Benchmark results.
     */
    @PostMapping("/benchmark")
    fun benchmark(@Valid @RequestBody input: BenchmarkInput): BenchmarkOutput {
        val maxN = max(input.listSizeA, input.listSizeB)

        // warmup
        repeat(10) {
            val listA = randomList(input.listSizeA, 0..maxN, uniqueElements = true)
            val listB = randomList(input.listSizeB, 0..maxN, uniqueElements = true)
            measureNanoTime {
                val res = service.intersectionBySize(listA, listB)
            }
        }

        val allTimesSmallToSet =
            (0 until input.iterations).map {
                val listA = randomList(input.listSizeA, 0..maxN, uniqueElements = true)
                val listB = randomList(input.listSizeB, 0..maxN, uniqueElements = true)
                measureNanoTime {
                        val res = service.intersectionBySize(listA, listB)
                    }
                    .toDouble() * 1e-3
            }

        val allTimesLargeToSet =
            (0 until input.iterations).map {
                val listA = randomList(input.listSizeA, 0..maxN, uniqueElements = true)
                val listB = randomList(input.listSizeB, 0..maxN, uniqueElements = true)
                measureNanoTime {
                        val res = service.intersectionBySize(listA, listB, smallerToSet = false)
                    }
                    .toDouble() * 1e-3
            }

        val timeSmallToSet = allTimesSmallToSet.average()
        val timeLargeToSet = allTimesLargeToSet.average()

        val stdSmallToSet =
            sqrt(
                allTimesSmallToSet.fold(0.0) { acc, value ->
                    acc + (value - timeSmallToSet).pow(2)
                } / (input.iterations - 1)
            )
        val stdLargeToSet =
            sqrt(
                allTimesLargeToSet.fold(0.0) { acc, value ->
                    acc + (value - timeLargeToSet).pow(2)
                } / (input.iterations - 1)
            )

        return BenchmarkOutput(
            input.listSizeA,
            input.listSizeB,
            timeSmallToSet,
            timeLargeToSet,
            stdSmallToSet / sqrt(input.iterations.toDouble()),
            stdLargeToSet / sqrt(input.iterations.toDouble()),
        )
    }

    /**
     * Calculates the intersection of two random lists of integers.
     *
     * @param input The Parameters for calculating the intersection, consisting of the list sizes
     *   and whether the first (or second) list should be converted to a set in the process.
     * @return Information on the elapsed time during the intersection calculation.
     */
    @PostMapping("/calculate")
    fun calculate(@Valid @RequestBody input: ExecutionInput): ExecutionOutput {
        val (listSizeA, listSizeB, aToSet) = input
        val maxSize = max(input.listSizeA, input.listSizeB) * 10
        val listA = randomList(listSizeA, 0..maxSize)
        val listB = randomList(listSizeB, 0..maxSize)
        val (left, right) = if (aToSet) Pair(listA, listB) else Pair(listB, listA)

        val time = measureNanoTime { service.intersection(left, right) }.toDouble() / 1000.0

        return ExecutionOutput(time)
    }
}
