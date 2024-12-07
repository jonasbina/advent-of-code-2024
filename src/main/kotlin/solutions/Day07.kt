package com.jonasbina.solutions

import com.jonasbina.utils.Day
import com.jonasbina.utils.InputUtils
import com.jonasbina.utils.Inputs
import com.jonasbina.utils.testPrintln

fun main() {
    val input = InputUtils.getDayInputText(7)
    val testInput = InputUtils.getTestInputText(7)
    val inputs = Inputs(input, testInput)
    Day07(inputs).run(correctResultPart1 = 3749L, correctResultPart2 = 11387L)
}

class Day07(
    override val inputs: Inputs
) : Day(inputs) {

    override fun part1(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        val equations = input.inputLines.map {
            val split = it.split(": ")
            val calib = split[0].toLong()
            val numbers = split[1].split(" ").map { it.toLong() }
            Equation(calib, numbers)
        }
        return equations.filter { equation ->
            var operators = IntArray(equation.numbers.size-1) { 0 }
            var equal = false
            while (true) {
                var total = 0L
                equation.numbers.forEachIndexed { i, n ->
                    if (i > 0) {
                        val operator = operators[i - 1]
                        if (operator == 1) {
                            total *= n
                        } else {
                            total += n
                        }
                    }else{
                        total+=n
                    }

                }
                //testPrintln((total to equation.calibrationNumber).toString(), test)
                if (total == equation.calibrationNumber) {
                    equal = true
                    break
                }
                if (operators.none {
                        it == 0
                    }) {
                    break
                }
                testPrintln(operators.toList().toString(),test)
                operators = addBinary(operators)
            }
            equal
        }.sumOf { it.calibrationNumber }
    }

    fun incrementBinaryArray(operators: IntArray): IntArray {
        for (i in operators.indices.reversed()) {
            if (operators[i] == 0) {
                operators[i] = 1
            } else {
                operators[i] = 0
            }
        }
        return operators
    }

    fun addBinary(intArray: IntArray): IntArray {
        // The two input Strings, containing the binary representation of the two values:


        // Use as radix 2 because it's binary
        val number0 = intArray.joinToString("").toInt(2)
        val sum = number0 + 1
        val output = Integer.toBinaryString(sum).padStart(intArray.size, '0')
        return IntArray(output.length) { output[it].digitToInt() } //returns the answer as a binary value;
    }
    fun addTrinary(intArray: IntArray): IntArray {
        // The two input Strings, containing the binary representation of the two values:


        // Use as radix 2 because it's binary
        val number0 = intArray.joinToString("").toInt(3)
        val sum = number0 + 1
        val output = Integer.toString(sum,3).padStart(intArray.size, '0')
        return IntArray(output.length) { output[it].digitToInt() } //returns the answer as a binary value;
    }

    override fun part2(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        val equations = input.inputLines.map {
            val split = it.split(": ")
            val calib = split[0].toLong()
            val numbers = split[1].split(" ").map { it.toLong() }
            Equation(calib, numbers)
        }
        return equations.filter { equation ->
            var operators = IntArray(equation.numbers.size-1) { 0 }
            var equal = false
            while (true) {
                var total = 0L
                equation.numbers.forEachIndexed { i, n ->
                    if (i > 0) {
                        val operator = operators[i - 1]
                        if (operator == 1) {
                            total *= n
                        } else if (operator==2){
                            total += n
                        }else{
                            total=(total.toString()+n.toString()).toLong()
                        }
                    }else{
                        total+=n
                    }

                }
                //testPrintln((total to equation.calibrationNumber).toString(), test)
                if (total == equation.calibrationNumber) {
                    equal = true
                    break
                }
                if (operators.all {
                        it == 2 //whoops needed to change that because of the trinary and ofc forgot
                    }) {
                    break
                }
                operators = addTrinary(operators)
            }
            equal
        }.sumOf { it.calibrationNumber }
    }

    private data class Equation(
        val calibrationNumber: Long,
        val numbers: List<Long>
    )
}