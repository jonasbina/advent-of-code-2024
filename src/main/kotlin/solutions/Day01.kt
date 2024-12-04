package com.jonasbina.solutions

import com.jonasbina.utils.Day
import com.jonasbina.utils.InputUtils
import com.jonasbina.utils.Inputs
import java.lang.Math.abs

fun main() {
    val input = InputUtils.getDayInputText(1)
    val testInput = InputUtils.getTestInputText(1)
    val inputs = Inputs(input, testInput)
    Day01(inputs).run(11,31)
}

class Day01(
    override val inputs:Inputs
): Day(inputs) {

    override fun part1(test:Boolean): Any {
        val input = if(test) inputs.testInput else inputs.input
        val split = input.inputLines.map { it.split("   ") }
        val firstList = split.map { it[0].toInt() }.sorted()
        val secondList = split.map { it[1].toInt() }.sorted()
        return firstList.zip(secondList).sumOf{ kotlin.math.abs(it.first - it.second) }

    }

    override fun part2(test: Boolean): Any {
        val input = if(test) inputs.testInput else inputs.input
        val split = input.inputLines.map { it.split("   ") }
        val firstList = split.map { it[0].toInt() }.sorted()
        val secondList = split.map { it[1].toInt() }.sorted()
        return firstList.sumOf { num -> secondList.count { it == num } * num }
    }
}