package com.jonasbina.solutions

import com.jonasbina.utils.*
import kotlin.math.truncate

fun main() {
    val input = InputUtils.getDayInputText(9)
    val testInput = InputUtils.getTestInputText(9)
    val inputs = Inputs(input, testInput)
    Day09(inputs).run(correctResultPart1 = 1928, correctResultPart2 = 0)
}

class Day09(
    override val inputs: Inputs
) : Day(inputs) {
    var string = ""
    override fun part1(test: Boolean): Any {
        val input = (if (test) inputs.testInput else inputs.input).input
        var file = true
        var i = 0
        var id = 0
        string = ""
        println("Reading input")
        input.forEach { c ->
            val digit = c.digitToInt()
            if (file) {
                for (x in 0..<digit) {
                    string += id
                }
                id++
            } else {
                for (x in 0..<digit) {
                    string += '.'
                }
            }
            i += digit
            file = !file
        }

        val ca = string.toCharArray()
        println("assembling")
        var dots = ca.mapIndexed { index, c -> c to index }.filter { it.first == '.' }.map { it.second }.toMutableList()
        var numbers = ca.mapIndexed { index, c -> c to index }.filter { it.first.isDigit() }.reversed().toMutableList()
        while (true) {
            val d = dots.first()
            val n = numbers.first()
            if (n.second < d) {
                break
            } else {
                numbers.remove(n)
                numbers.add(n.first to d)
                dots.remove(d)
                dots.add(n.second)
                dots.sort()
                numbers.sortByDescending {
                    it.second
                }
                ca[d] = n.first
                ca[n.second] = '.'
            }
        }
        println("calculating")
        var sum = 0L
        ca.filter { it.isDigit() }.forEachIndexed { ind, c ->
            sum+=c.digitToInt() * ind
        }
        return sum
    }

    override fun part2(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        return 0
    }
}