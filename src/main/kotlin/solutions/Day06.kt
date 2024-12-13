package com.jonasbina.solutions

import com.jonasbina.utils.*

fun main() {
    val input = InputUtils.getDayInputText(6)
    val testInput = InputUtils.getTestInputText(6)
    val inputs = Inputs(input, testInput)
    Day06(inputs).run(correctResultPart1 = 41, correctResultPart2 = 6)
}

class Day06(
    override val inputs: Inputs
) : Day(inputs) {
    lateinit var positions: MutableSet<Point2D>
    override fun part1(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        val index = input.input.indexOf('^')
        val pos = if (index == 70) {
            Point2D(4, 6)
        } else {
            Point2D(91, 69)
        }
        val charsPerLine = input.inputLines[0].length - 1

        Point2D(index / charsPerLine, index % charsPerLine)
        println(pos)
        var currentPosition = pos
        var currentMove = Move.up
        positions = mutableSetOf(pos)
        while (true) {
            currentPosition = currentPosition.applyMove(currentMove)
            if (!currentPosition.isInRange(input.inputLines[0].length, input.inputLines.size)) {
                break
            }
            positions.add(currentPosition)
            val inFront = currentPosition.applyMove(currentMove)
            if (Point2D(inFront.x, inFront.y).isInRange(input.inputLines[0].length, input.inputLines.size)) {
                if (input.inputLines[inFront.y][inFront.x] == '#') {
                    currentMove = when (currentMove) {
                        Move.up -> Move.right
                        Move.down -> Move.left
                        Move.left -> Move.up
                        Move.right -> Move.down
                        else -> Move.up
                    }
                }
            }
        }
        return positions.size
    }

    override fun part2(test: Boolean): Any {
        //1309 correct answer
        val input = if (test) inputs.testInput else inputs.input
        val index = input.input.indexOf('^')
        val pos = if (index == 70) {
            Point2D(4, 6)
        } else {
            Point2D(91, 69)
        }


        val result = (positions - pos).count {
            val changedInput = input.inputLines.toMutableList()
            val currentRow = changedInput[it.y]
            changedInput[it.y] = currentRow.take(it.x) + '#' + currentRow.drop(it.x + 1)
            simulateOnInput(changedInput, pos)
        }
        return result
    }

    fun simulateOnInput(inputLines: List<String>, pos: Point2D): Boolean {
        var currentPosition = pos
        var currentMove = Move.up
        val positions = mutableSetOf<Pair<Point2D, Move>>()
        while (true) {
            if (positions.contains(currentPosition to currentMove)) {
                return true
            }
            positions.add(currentPosition to currentMove)

            val toGo = currentPosition.applyMove(currentMove)
            if (!toGo.isInRange(inputLines[0].length, inputLines.size)) {
                return false
            }
            if (inputLines[toGo.y][toGo.x] != '#') {
                currentPosition = toGo
                val inFront = currentPosition.applyMove(currentMove)
                if (inFront.isInRange(inputLines[0].length, inputLines.size)) {
                    if (inputLines[inFront.y][inFront.x] == '#') {
                        currentMove = currentMove.turn90Deg()
                    }
                }
            }else{
                currentMove = currentMove.turn90Deg()
            }
        }
    }

}