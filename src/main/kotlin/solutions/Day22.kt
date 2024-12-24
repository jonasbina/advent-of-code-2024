package com.jonasbina.solutions

import com.jonasbina.utils.Day
import com.jonasbina.utils.InputUtils
import com.jonasbina.utils.Inputs

fun main() {
    val input = InputUtils.getDayInputText(22)
    val testInput = InputUtils.getTestInputText(22)
    val inputs = Inputs(input, testInput)
    Day22(inputs).run(correctResultPart1 = 37327623L, correctResultPart2 = 24)
}

class Day22(
    override val inputs: Inputs
) : Day(inputs) {

    override fun part1(test: Boolean): Any {
        val inputLines = if (test) inputs.testInput.inputLines else inputs.input.inputLines
        return inputLines.sumOf {
            var res = it.toLong()
            repeat(2000) {
                res = (res.toLong().xor(res * 64L) % 16777216L)
                res = (res.toLong().xor(res / 32L) % 16777216L)
                res = (res.toLong().xor(res * 2048L) % 16777216L)
            }
            res
        }
    }

    override fun part2(test: Boolean): Any {
        val inputLines = if (test) inputs.testInput.inputLines else inputs.input.inputLines
        val priceChanges = inputLines.map {
            var res = it.toLong()
            (1..2000).toList().map {
                var newRes = res
                newRes = (newRes.toLong().xor(newRes * 64L) % 16777216L)
                newRes = (newRes.toLong().xor(newRes / 32L) % 16777216L)
                newRes = (newRes.toLong().xor(newRes * 2048L) % 16777216L)
                val result =
                    newRes.toString().last().digitToInt() to newRes.toString().last().digitToInt() - res.toString()
                        .last().digitToInt()
                res = newRes
                result
            }
        }
        val map = mutableMapOf<List<Int>, Int>()
        priceChanges.forEach { it ->
            val alreadyFound = mutableSetOf<List<Int>>()
            it.windowed(4, 1).forEach { window ->
                val list = window.map { it.second }
                if (list !in alreadyFound) {
                    if (map.containsKey(list)) {
                        map[list] = map[list]!! + window.last().first
                    } else {
                        map[list] = window.last().first
                    }
                    alreadyFound.add(list)
                }
            }
        }

        return map.maxBy { it.value }.value
    }
}