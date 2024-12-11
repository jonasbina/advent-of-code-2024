package com.jonasbina.solutions

import com.jonasbina.utils.Day
import com.jonasbina.utils.InputUtils
import com.jonasbina.utils.Inputs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main() {
    val input = InputUtils.getDayInputText(11)
    val testInput = InputUtils.getTestInputText(11)
    val inputs = Inputs(input, testInput)
    Day11(inputs).run(correctResultPart1 = 55312L, correctResultPart2 = 65601038650482L)
}
// run time 0.004s for part1 and 0.095s for part2
class Day11(
    override val inputs: Inputs
) : Day(inputs) {

    override fun part1(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        return calculateStoneSize(input.input, 25)
    }

    fun calculateStoneSize(input: String, repeatTimes: Int): Long {
        val stonesList = input.split(" ").map { it.toLong() }
        val stones = LongArray(stonesList.size){stonesList[it]}
        var map = mutableMapOf<Long,Long>()
        stones.forEach { stone ->
            map[stone] = (map[stone]?:0L) + 1L
        }
        repeat(repeatTimes) {
            val newMap = mutableMapOf<Long,Long>()
            map.forEach { (t, u) ->
                val newList = t.blink()
                newList.forEach {newVal->
                    newMap[newVal]=(newMap[newVal]?:0L) + u
                }
            }
            map = newMap
        }
        return map.map {
            it.value
        }.sum()
    }

    override fun part2(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        return calculateStoneSize(input.input, 75)
    }


    fun Long.blink(): List<Long> {
        val stringNum = this.toString()
        if (this == 0L) {
            return listOf(1)
        }
        if (stringNum.length % 2 == 0) {
            val halfLength = stringNum.length / 2
            val firstPart = stringNum.substring(0, halfLength).toLong()
            val secondPart = stringNum.substring(halfLength).toLong()
            return listOf(firstPart, secondPart)
        }
        return listOf(this * 2024L)
    }

}