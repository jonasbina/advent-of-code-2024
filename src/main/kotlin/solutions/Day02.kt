package com.jonasbina.solutions

import com.jonasbina.utils.Day
import com.jonasbina.utils.InputUtils
import com.jonasbina.utils.Inputs
import java.text.Bidi
import kotlin.math.abs

fun main() {
    val input = InputUtils.getDayInputText(2)
    val testInput = InputUtils.getTestInputText(2)
    val inputs = Inputs(input, testInput)
    Day02(inputs).run(correctResultPart1 = 2, correctResultPart2 = 4)
}

class Day02(
    override val inputs: Inputs
) : Day(inputs) {

    override fun part1(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        val inputInts = input.inputLines.map { it.split(" ").map { x -> x.toInt() } }
        return inputInts.count { case ->
            var response = -1 //-1 - null, 0 - false, 1 - true
            var direction = -1 //-1 - null, 0 - down, 1 - up
            var lastInt: Int? = null
            var index = 0
            while (response == -1 && index < case.size) {
                val currentInt = case[index]
                if (lastInt != null) {
                    val difference = abs(lastInt - currentInt)
                    if (difference !in 1..3) {
                        response = 0
                    } else {
                        if (lastInt < currentInt) {
                            if (direction != 1) {
                                direction = 0
                            } else {
                                response = 0
                            }
                        } else {
                            if (lastInt > currentInt) {
                                if (direction != 0) {
                                    direction = 1
                                } else {
                                    response = 0
                                }
                            }
                        }
                    }
                }
                lastInt = currentInt
                index++
            }
            response != 0
        }
    }

    override fun part2(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        val inputInts = input.inputLines.map { it.split(" ").map { x -> x.toInt() } }
        return inputInts.count { case ->
            calculateCase(case)
        }
    }

    fun calculateCase(case: List<Int>, cycle: Int = 0): Boolean {
        var response = -1 //-1 - null, 0 - false, 1 - true
        var direction = -1 //-1 - null, 0 - down, 1 - up
        var lastInt: Int? = null
        var index = 0
        while (response == -1 && index < case.size) {
            val currentInt = case[index]
            if (lastInt != null) {
                val difference = abs(lastInt - currentInt)
                if (difference !in 1..3) {
                    if (cycle == 0) {
                        for (i in 0..index) {
                            if (calculateCase(case.take(i) + case.takeLast(case.lastIndex - i), 1)) {
                                response = 1
                            }
                        }
                        if (response!=1){
                            response=0
                        }
                    } else {
                        response = 0
                    }
                } else {
                    if (lastInt < currentInt) {
                        if (direction != 1) {
                            direction = 0
                        } else {
                            if (cycle == 0) {
                                for (i in 0..index) {
                                    if (calculateCase(case.take(i) + case.takeLast(case.lastIndex - i), 1)) {
                                        response = 1
                                    }
                                }
                                if (response!=1){
                                    response=0
                                }
                            } else {
                                response = 0
                            }
                        }
                    } else {
                        if (lastInt > currentInt) {
                            if (direction != 0) {
                                direction = 1
                            } else {
                                if (cycle == 0) {
                                    for (i in 0..index) {
                                        if (calculateCase(case.take(i) + case.takeLast(case.lastIndex - i), 1)) {
                                            response = 1
                                        }
                                    }
                                    if (response!=1){
                                        response=0
                                    }
                                } else {
                                    response = 0
                                }
                            }
                        }
                    }
                }
            }
            lastInt = currentInt
            index++
        }
        return response != 0
    }
}