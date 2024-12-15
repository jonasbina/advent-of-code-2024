package com.jonasbina.solutions

import com.jonasbina.utils.*
import kotlin.properties.Delegates

fun main() {
    val input = InputUtils.getDayInputText(14)
    val testInput = InputUtils.getTestInputText(14)
    val inputs = Inputs(input, testInput)
    Day14(inputs).run(correctResultPart1 = 12, correctResultPart2 = 0)
}

private var fieldHeight by Delegates.notNull<Int>()
private var fieldWidth by Delegates.notNull<Int>()

class Day14(
    override val inputs: Inputs
) : Day(inputs) {
    var robots = listOf<Robot>()
    override fun part1(test: Boolean): Any {
        fieldWidth = if (test) 11 else 101
        fieldHeight = if (test) 7 else 103
        val input = if (test) inputs.testInput else inputs.input
        loadRobots(input.inputLines)
        repeat(100) {
            robots.forEach {
                it.apply1Second()
            }
        }
        var res = 1
        groupPositionsIntoQuadrants(robots).forEach { t, u ->
            res *= u.size
        }
        return res
    }

    override fun part2(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        loadRobots(input.inputLines)
        var result = 0
        var s = 1
        if (!test) {
            while (true) {
                robots.forEach {
                    it.apply1Second()
                }
                if (containsFilledTriangle(robots.map { it.position })) {
                    result = s
                    break
                }
                s++
            }
        }

        return result
    }

    private fun loadRobots(inputLines: List<String>) {
        robots = inputLines.map { line ->
            val split = line.split(" ")
            val pos = split[0].removePrefix("p=").split(",").map { it.toInt() }
            val vel = split[1].removePrefix("v=").split(",").map { it.toInt() }
            Robot(Point2D(pos[0], pos[1]), Move(vel[0], vel[1]))
        }
    }

    fun groupPositionsIntoQuadrants(
        robots: List<Robot>,
    ): Map<String, List<Robot>> {
        val midX = fieldWidth / 2
        val midY = fieldHeight / 2
        val quadrants = mutableMapOf(
            "TopLeft" to mutableListOf<Robot>(),
            "TopRight" to mutableListOf<Robot>(),
            "BottomLeft" to mutableListOf<Robot>(),
            "BottomRight" to mutableListOf<Robot>()
        )

        for (r in robots) {
            val x = r.position.x
            val y = r.position.y
            when {
                x < midX && y < midY -> quadrants["TopLeft"]?.add(r)
                x > midX && y < midY -> quadrants["TopRight"]?.add(r)
                x < midX && y > midY -> quadrants["BottomLeft"]?.add(r)
                x > midX && y > midY -> quadrants["BottomRight"]?.add(r)
            }
        }

        return quadrants
    }

    fun containsFilledTriangle(points: List<Point2D>): Boolean {
        val pointSet = points.toSet()
        for (p in points) {
            var currentRow = listOf(p)
            var layerSize = 1

            while (currentRow.isNotEmpty()) {
                if (currentRow.all { it in pointSet }) {
                    val nextRow = currentRow.flatMap {
                        listOf(Point2D(it.x - 1, it.y + 1), Point2D(it.x + 1, it.y + 1))
                    }.distinct()

                    layerSize += 1
                    currentRow = nextRow
                } else {
                    break
                }
            }
            if (layerSize > 5) return true
        }

        return false
    }

    data class Robot(
        var position: Point2D,
        val move: Move
    ) {
        fun apply1Second() {
            position = position.applyMove(move)
            if (position.x < 0) {
                position = Point2D(fieldWidth + position.x, position.y)
            }
            if (position.x >= fieldWidth) {
                position = Point2D(position.x - fieldWidth, position.y)
            }
            if (position.y < 0) {
                position = Point2D(position.x, fieldHeight + position.y)
            }
            if (position.y >= fieldHeight) {
                position = Point2D(position.x, position.y - fieldHeight)
            }
        }
    }
}