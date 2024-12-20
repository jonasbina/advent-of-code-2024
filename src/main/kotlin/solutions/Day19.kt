package com.jonasbina.solutions

import com.jonasbina.utils.Day
import com.jonasbina.utils.InputUtils
import com.jonasbina.utils.Inputs

fun main() {
    val input = InputUtils.getDayInputText(19)
    val testInput = InputUtils.getTestInputText(19)
    val inputs = Inputs(input, testInput)
    Day19(inputs).run(correctResultPart1 = 6, correctResultPart2 = 16)
}

class Day19(
    override val inputs:Inputs
): Day(inputs){

    override fun part1(test: Boolean): Any {
        val inputLines = if (test) inputs.testInput.inputLines else inputs.input.inputLines
        val blocks = inputLines[0].split(", ").toSet()
        val combinations = inputLines.drop(2)
        val memo = mutableMapOf<String, Boolean>()

        fun canForm(combination: String): Boolean {
            memo[combination]?.let { return it }
            if (combination.isEmpty()) return true
            val result = blocks.any { block ->
                combination.startsWith(block) && canForm(combination.substring(block.length))
            }
            memo[combination] = result
            return result
        }

        return combinations.count { combination ->
            canForm(combination)
        }
    }

    override fun part2(test:Boolean): Any {
        val inputLines = if (test) inputs.testInput.inputLines else inputs.input.inputLines
        val blocks = inputLines[0].split(", ").toSet()
        val combinations = inputLines.drop(2)
        val memo = mutableMapOf<String, Long>()
        fun countWays(combination: String): Long {
            memo[combination]?.let { return it }
            if (combination.isEmpty()) return 1
            val totalWays = blocks
                .filter { combination.startsWith(it) }
                .sumOf { block ->
                    countWays(combination.substring(block.length))
                }
            memo[combination] = totalWays
            return totalWays
        }
        return combinations.sumOf { combination ->
            countWays(combination)
        }
    }
}