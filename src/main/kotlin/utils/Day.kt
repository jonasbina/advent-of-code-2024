package com.jonasbina.utils

import kotlin.time.measureTime

open class Day(
    open val inputs: Inputs
){
    open fun part1(test: Boolean = false): Any {
        return 0
    }

    open fun part2(test: Boolean = false): Any {
        return 0
    }

    fun run(correctResultPart1: Any = 0, correctResultPart2: Any) {
        lateinit var testPart1Result: Any
        lateinit var testPart2Result: Any
        lateinit var part1Result: Any
        lateinit var part2Result: Any

        println("=== Starting Tests ===\n")

        println("TEST Part 1:")
        val testPart1Time = measureTime {
            testPart1Result = part1(true)
        }.inWholeMilliseconds.toFloat() / 1000
        println("Execution Time: ${testPart1Time}s")
        println("Result: $testPart1Result")
        println(if (testPart1Result == correctResultPart1) "Test Passed!" else "Test Failed - ${correctResultPart1}!=$testPart1Result")
        println()

        println("TEST Part 2:")
        val testPart2Time = measureTime {
            testPart2Result = part2(true)
        }.inWholeMilliseconds.toFloat() / 1000
        println("Execution Time: ${testPart2Time}s")
        println("Result: $testPart2Result")
        println(if (testPart2Result == correctResultPart2) "Test Passed!" else "Test Failed - ${correctResultPart2}!=$testPart2Result")
        println()

        println("=== Tests Ended, Starting Actual Puzzles ===\n")

        println("ACTUAL Part 1:")
        val part1Time = measureTime {
            part1Result = part1()
        }.inWholeMilliseconds.toFloat() / 1000
        println("Execution Time: ${part1Time}s")
        println("Result: $part1Result")
        println()

        println("ACTUAL Part 2:")
        val part2Time = measureTime {
            part2Result = part2()
        }.inWholeMilliseconds.toFloat() / 1000
        println("Execution Time: ${part2Time}s")
        println("Result: $part2Result")
        println()

        println("=== End of the Run ===\n")
    }
}
