package com.jonasbina.solutions

import com.jonasbina.utils.Day
import com.jonasbina.utils.InputUtils
import com.jonasbina.utils.Inputs

fun main() {
    val input = InputUtils.getDayInputText(24)
    val testInput = InputUtils.getTestInputText(24)
    val inputs = Inputs(input, testInput)
    Day24(inputs).run(correctResultPart1 = 2024L, correctResultPart2 = 0)
}

class Day24(
    override val inputs: Inputs
) : Day(inputs) {
    var gates = mutableListOf<Gate>()
    override fun part1(test: Boolean): Any {
        val input = if (test) inputs.testInput.input else inputs.input.input
        val (initialValues, conditions) = input.split("\n\n").map { it.split("\n") }
        gates = conditions.map {
            val (value1, operator, value2, _, saveTo) = it.split(" ")
            Gate(value1, value2, saveTo, operator)
        }.toMutableList()
        return simulate(initialValues.map {
            it.split(": ")[0] to it.split(": ")[1].toInt()
        }.toMap(), gates).filterKeys { it.startsWith("z") }.toList().sortedByDescending { it.first }
            .joinToString("") { it.second.toString() }.toLong(2)
    }

    fun String.toAction(): Action {
        return when (this) {
            "XOR" -> XOR()
            "AND" -> AND()
            "OR" -> OR()
            else -> OR()
        }
    }

    private fun simulate(initialValues: Map<String, Int>, gateList: List<Gate>): Map<String, Int> {
        val result = initialValues.toMutableMap()
        val pending = gateList.toMutableList()

        while (pending.isNotEmpty()) {
            val processable = pending.filter {
                result.containsKey(it.input1) && result.containsKey(it.input2)
            }
            if (processable.isEmpty()) break

            processable.forEach { gate ->
                val value1 = result[gate.input1]!!
                val value2 = result[gate.input2]!!
                result[gate.output] = gate.operation.toAction().apply(value1, value2)
                pending.remove(gate)
            }
        }
        return result
    }

    private fun isValidAdder(initialValues: Map<String, Int>, gateList: List<Gate>): Boolean {
        val result = simulate(initialValues, gateList)
        val xBits = initialValues.filterKeys { it.startsWith("x") }
            .toList().sortedBy { it.first }.map { it.second }
        val yBits = initialValues.filterKeys { it.startsWith("y") }
            .toList().sortedBy { it.first }.map { it.second }
        val zBits = result.filterKeys { it.startsWith("z") }
            .toList().sortedBy { it.first }.map { it.second }
        val x = xBits.joinToString("").toLong(2)
        val y = yBits.joinToString("").toLong(2)
        val z = zBits.joinToString("").toLong(2)

        return z == x + y
    }

    override fun part2(test: Boolean): Any {
        if (test) return 0
        val input = if (test) inputs.testInput.input else inputs.input.input
        val (initialValues, conditions) = input.split("\n\n").map { it.split("\n") }
        var gates = conditions.map {
            val (value1, operator, value2, _, saveTo) = it.split(" ")
            Gate(value1, value2, saveTo, operator)
        }.toMutableList()
        val swapped = mapOf<String,String>(
//            "z09" to "z10",
//            "z10" to "z09"
        )
        gates = gates.map {
            if (it.output in swapped){
                it.copy(output = swapped[it.output]!!)
            }else{
                it
            }
        }.toMutableList()

        val binaryOnes = 9
        val correct = ("1".repeat(binaryOnes).toLong(2)*2).toString(2)
        val values = initialValues.groupBy { it.first() }.map { (t, u) ->
            u.map {v->
                val split = v.split(": ")
                val number = split[0].drop(1).toInt()
                split[0] to if (number<binaryOnes) 1 else 0
            }
        }.flatten().toMap()
        //10111111110 program
        //01111111110 correct
        //binary 9 - a move from z09 to z10 -changed it manually
        //binary 15
        println(correct)
        val output = simulate(values, gates).filterKeys { it.startsWith("z") }.toList().sortedByDescending { it.first }.joinToString(""){it.second.toString()}.dropWhile { it!='1' }
        println(output)
        return output
    }
    fun <T> List<T>.generateFourPairsCombinations(): List<List<Pair<T, T>>> {
        // Need at least 8 elements to make 4 pairs
        if (size < 8) return emptyList()

        val result = mutableListOf<List<Pair<T, T>>>()

        // Generate all possible pairs first
        val allPairs = mutableListOf<Pair<T, T>>()
        for (i in indices) {
            for (j in i + 1 until size) {
                allPairs.add(this[i] to this[j])
            }
        }

        // Now generate combinations of 4 pairs where no element is used twice
        for (i in allPairs.indices) {
            val pair1 = allPairs[i]
            val used1 = setOf(pair1.first, pair1.second)

            for (j in i + 1 until allPairs.size) {
                val pair2 = allPairs[j]
                if (pair2.first in used1 || pair2.second in used1) continue
                val used2 = used1 + setOf(pair2.first, pair2.second)

                for (k in j + 1 until allPairs.size) {
                    val pair3 = allPairs[k]
                    if (pair3.first in used2 || pair3.second in used2) continue
                    val used3 = used2 + setOf(pair3.first, pair3.second)

                    for (l in k + 1 until allPairs.size) {
                        val pair4 = allPairs[l]
                        if (pair4.first in used3 || pair4.second in used3) continue

                        result.add(listOf(pair1, pair2, pair3, pair4))
                    }
                }
            }
        }

        return result
    }

    data class Gate(
        val input1: String,
        val input2: String,
        val output: String,
        val operation: String
    )

    open class Action {
        open fun apply(int: Int, int1: Int): Int {
            return int
        }
    }

    class XOR : Action() {
        override fun apply(int: Int, int1: Int): Int {
            return if (int != int1) 1 else 0
        }
    }

    class OR : Action() {
        override fun apply(int: Int, int1: Int): Int {
            return if (int == 1 || int1 == 1) 1 else 0
        }
    }

    class AND : Action() {
        override fun apply(int: Int, int1: Int): Int {
            return if (int == 1 && int1 == 1) 1 else 0
        }
    }
}