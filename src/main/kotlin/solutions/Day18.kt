package com.jonasbina.solutions

import com.jogamp.newt.event.MouseEvent.PointerType
import com.jonasbina.utils.*
import processing.core.PApplet

fun main() {
    val input = InputUtils.getDayInputText(18)
    val testInput = InputUtils.getTestInputText(18)
    val inputs = Inputs(input, testInput)
    Day18(inputs).run(correctResultPart1 = 22, correctResultPart2 = "6,1")
}

class Day18(
    override val inputs: Inputs
) : Day(inputs) {

    override fun part1(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        val barriers = input.inputLines.mapIndexed { i, line ->
            val split = line.trim().split(",").map {
                it.toInt()
            }
            Point2D(split[0], split[1])
        }.take(if (test) 12 else 1024)
        return solveForBarriers(test, barriers)?.cost ?: 0
    }

    override fun part2(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        val barriers = input.inputLines.mapIndexed { i, line ->
            val split = line.trim().split(",").map {
                it.toInt()
            }
            Point2D(split[0], split[1])
        }
        var size = if (test) 12 else 1024
        var finishingCoord = Point2D(0, 0)
        while (size < barriers.size) {
            val s = solveForBarriers(test, barriers.take(size))
            if (s == null) {
                finishingCoord = barriers.take(size).last()
                break
            }
            size = barriers.indexOfFirst {
                it in s.path
            }+1
        }
        return finishingCoord.x.toString() + "," + finishingCoord.y
    }
    fun solveForBarriers(test: Boolean, barriers: List<Point2D>): AStarResult<Point2D>? {
        val start = Point2D(0, 0)
        val end = if (test) Point2D(6, 6) else Point2D(70, 70)
        return aStarSearch(start, isEnd = { end == it }, next = {
            listOf(it.right(), it.left(), it.up(), it.down()).filter { x ->
                val contains = barriers.contains(x)
                val inRange = x.isInRange(end.x + 1)
                inRange && !contains
            }.map { aaa ->
                aaa to 1
            }
        }, { 0 })
    }
}