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
        return "i dont wanna lose my sanity"
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