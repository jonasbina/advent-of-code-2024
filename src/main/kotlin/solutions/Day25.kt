package com.jonasbina.solutions

import com.jonasbina.utils.Day
import com.jonasbina.utils.InputUtils
import com.jonasbina.utils.Inputs
import com.jonasbina.utils.TestPrintler

fun main() {
    val input = InputUtils.getDayInputText(25)
    val testInput = InputUtils.getTestInputText(25)
    val inputs = Inputs(input, testInput)
    Day25(inputs).run(correctResultPart1 = 3L, correctResultPart2 = 0)
}

class Day25(
    override val inputs:Inputs
): Day(inputs){
    
    override fun part1(test:Boolean): Any {
        val tp = TestPrintler(test)
        val input = if(test) inputs.testInput.input else inputs.input.input
        val schematics = input.split("\n\n").map {
            val lines = it.split("\n")
            val columns = Array(lines[0].length) { Array(lines.size) { ' ' } }
            for (y in 0 until lines.size) {
                for (x in 0 until lines[y].length){
                    columns[x][y] = lines[y][x]
                }
            }
            columns.map { it.toList() }.toList()
        }
        val keys = schematics.filter {
            it.map { it.last() }.all {
                it == '#'
            }
        }
        val locks = schematics.filter {
            it.map { it[0] }.all {
                it == '#'
            }
        }
        var count = 0
        locks.forEach { lock ->
            keys.forEach { key ->
                var correct = true
                for (i in 0 until key.size){
                    if(lock[i].filter { it=='#' }.size + key[i].filter { it=='#' }.size>lock[i].size){
                        correct=false
                    }
                }
                if (correct){
                    count++
                }
            }
        }

        return count
    }

    override fun part2(test:Boolean): Any {
        val input = if(test) inputs.testInput else inputs.input
        return 0
    }
}