package com.jonasbina.solutions

import com.jonasbina.utils.Day
import com.jonasbina.utils.InputUtils
import com.jonasbina.utils.Inputs

fun main() {
    val input = InputUtils.getDayInputText(3)
    val testInput = InputUtils.getTestInputText(3)
    val inputs = Inputs(input, testInput)
    Day03(inputs).run(correctResultPart1 = 161, correctResultPart2 = 48)
}

class Day03(
    override val inputs:Inputs
): Day(inputs){
    
    override fun part1(test:Boolean): Any {
        val input = if(test) inputs.testInput else inputs.input
        val regex = Regex("mul\\(\\d{1,3},\\d{1,3}\\)")
        return regex.findAll(input.input).sumOf {
            val mult = it.value.split("(")[1].dropLast(1).split(",").map { it.toInt() }
            mult[0]*mult[1]
        }
    }

    override fun part2(test:Boolean): Any {
        val input = if(test) inputs.testInput else inputs.input
        val regex = Regex("mul\\(\\d{1,3},\\d{1,3}\\)")
        val doRegex = Regex("do\\(\\)")
        val dontRegex = Regex("don't\\(\\)")
        var lastIndex = -1
        val allMuls =regex.findAll(input.input).toList()

        val mults = allMuls.map {
            val mult = it.value.split("(")[1].dropLast(1).split(",").map { it.toInt() }
            val index = input.input.indexOf(it.value)
            lastIndex=index
            Mult(index,mult[0]*mult[1])
        }
        lastIndex=-1
        val dos = doRegex.findAll(input.input).toList().map {
            val index = input.input.drop(lastIndex+2).indexOf(it.value)+lastIndex+2
            lastIndex=index
            Do(index)
        }
        lastIndex=-1
        val donts = dontRegex.findAll(input.input).toList().map {
            val index = input.input.drop(lastIndex+2).indexOf(it.value)+lastIndex+2
            lastIndex=index
            Dont(index)
        }
        val all = (dos+donts+mults).sortedBy { it.index }
        var result = 0
        var enabled = true
        all.forEach {
            if (it is Dont){
                enabled = false
            }
            if (it is Do){
                enabled=true
            }
            if (it is Mult&&enabled){
                result+=it.result
            }
        }
        return result
    }
}
private open class Day03Item(open val index:Int)
private data class Do(override val index:Int): Day03Item(index)
private data class Dont(override val index:Int): Day03Item(index)
private data class Mult(override val index:Int, val result:Int): Day03Item(index)