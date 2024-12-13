package com.jonasbina.solutions

import com.jonasbina.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

fun main() {
    val input = InputUtils.getDayInputText(13)
    val testInput = InputUtils.getTestInputText(13)
    val inputs = Inputs(input, testInput)
    Day13(inputs).run(correctResultPart1 = 480L, correctResultPart2 = 0)
}

class Day13(
    override val inputs: Inputs
) : Day(inputs) {
    var processedInputs = mutableListOf<ProcessedInput>()
    private lateinit var tp: TestPrintler
    override fun part1(test: Boolean): Any {
        tp = TestPrintler(test)
        val input = if (test) inputs.testInput else inputs.input
        processInput(input)
        return calculateMinCost()
    }

    fun calculateMinCost(): Long {
        return runBlocking {
            processedInputs.sumOf { (firstMove, secondMove, goal, firstP, secondP) ->
                withContext(Dispatchers.Default) {
                    val det = firstMove.dx * secondMove.dy - firstMove.dy * secondMove.dx;
                    val b = (firstMove.dx * goal.y - firstMove.dy * goal.x) / det
                    val distXB = secondMove.dx * b
                    val distXA = goal.x-distXB
                    val a = distXA/firstMove.dx
                    if (Point2DLong(
                            firstMove.dx * a + secondMove.dx * b,
                            firstMove.dy * a + secondMove.dy * b
                        ) == goal
                    ) {
                        println(a to b)
                        3 * a + b
                    }else{
                        0L
                    }
                }
            }
        }
    }

    fun processInput(input: Input, additionToPrizeCoords: Long = 0) {
        processedInputs = input.input.split("\n\n").map { it.split("\n") }.mapNotNull { part ->
            if (part.size == 3) {
                val b1 = part[0]
                val b2 = part[1]
                val y1 = b1.split("Y+")[1].toInt()
                val y2 = b2.split("Y+")[1].toInt()
                val x1 = b1.split("X+")[1].split(",")[0].toInt()
                val x2 = b2.split("X+")[1].split(",")[0].toInt()
                val prize = part[2]
                val px = prize.split("X=")[1].split(",")[0].toInt() + additionToPrizeCoords
                val py = prize.split("Y=")[1].toInt() + additionToPrizeCoords
                ProcessedInput(
                    firstMove = Move(x1, y1 ),
                    secondMove = Move(x2, y2),
                    goal = Point2DLong(px, py),
                    3,
                    1
                )
            } else {
                null
            }
        }.toMutableList()
    }

    override fun part2(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        processInput(input, 10_000_000_000_000)
        return calculateMinCost()
    }

    @Serializable
    data class ProcessedInput(
        val firstMove: Move,
        val secondMove: Move,
        val goal: Point2DLong,
        val firstPrice: Int,
        val secondPrice: Int
    )
}