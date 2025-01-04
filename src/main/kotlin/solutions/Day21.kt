package com.jonasbina.solutions

import com.jonasbina.utils.*

fun main() {
    val input = InputUtils.getDayInputText(21)
    val testInput = InputUtils.getTestInputText(21)
    val inputs = Inputs(input, testInput)
    Day21(inputs).run(correctResultPart1 = 126384L, correctResultPart2 = 0)
}

class Day21(
    override val inputs: Inputs
) : Day(inputs) {
    val numericKeyboardMap = mapOf(
        '7' to Point2D(0, 0),
        '8' to Point2D(1, 0),
        '9' to Point2D(2, 0),
        '4' to Point2D(0, 1),
        '5' to Point2D(1, 1),
        '6' to Point2D(2, 1),
        '1' to Point2D(0, 2),
        '2' to Point2D(1, 2),
        '3' to Point2D(2, 2),
        '0' to Point2D(1, 3),
        'A' to Point2D(2, 3),
    )
    val arrowKeyboardMap = mapOf(
        '0' to Point2D(1, 0),
        '2' to Point2D(0, 1),
        '1' to Point2D(1, 1),
        '3' to Point2D(2, 1),
        '4' to Point2D(2, 0),
    )

    override fun part1(test: Boolean): Any {
        val inputLines = if (test) inputs.testInput.inputLines else inputs.input.inputLines
        return inputLines.sumOf { s ->
            var l = s.convertUsingNumpad().split(aRegex).groupingBy { it }.eachCount().map { it.key to it.value.toLong() }.toMap()
            repeat(2){
                l = l.convertUsingKeyboard()
            }
            s.filter { it.isDigit() }.toLong() * l.toList().sumOf {
                it.second*it.first.length
            }
        }
    }
    var uniqueCombinations = mutableListOf<String>()
    val chunkMemo = mutableMapOf<String, String>()
    val aRegex = Regex("(?<=4)")
    private fun Map<String,Long>.convertUsingKeyboard(): Map<String,Long> {
        var cur='4'
        val nextMap = mutableMapOf<String,Long>()
        this.forEach { t, u ->
            chunkMemo.getOrPut(t) {
                t.map { c ->
                    val r = memo.getOrPut(cur to c) { findPath(arrowKeyboardMap[cur]!!, arrowKeyboardMap[c]!!, false) }
                    cur = c
                    r
                }.joinToString("")
            }.split(aRegex).forEach {
                nextMap[it] = (nextMap[it]?:0L) + u
            }
        }
        return nextMap
    }
    private fun String.convertUsingNumpad(): String {
        var cur = numericKeyboardMap['A']!!
        return this.map { c ->
            val end = numericKeyboardMap[c]!!
            val r = findPath(cur, end, true)
            cur = end
            r
        }.joinToString("")
    }

    val memo = mutableMapOf<Pair<Char, Char>, String>()
    fun findPath(current: Point2D, end: Point2D, numpad: Boolean): String {
        val xMoves = mutableListOf<Move>()
        val yMoves = mutableListOf<Move>()
        if (current.x < end.x) {
            repeat(end.x - current.x) {
                xMoves.add(Move.right)
            }
        }
        if (current.x > end.x) {
            repeat(current.x - end.x) {
                xMoves.add(Move.left)
            }
        }
        if (current.y < end.y) {
            repeat(end.y - current.y) {
                yMoves.add(Move.down)
            }
        }
        if (current.y > end.y) {
            repeat(current.y - end.y) {
                yMoves.add(Move.up)
            }
        }
        var path: List<Move> = listOf()
        if (numpad) {
            if (current.y == 3 && end.x == 0) {
                path = yMoves + xMoves
            } else {
                if (current.x == 0 && end.y == 3) {
                    path = xMoves + yMoves
                } else {
                    if (xMoves.isNotEmpty() && yMoves.isNotEmpty()) {
                        if (xMoves[0] == Move.left) {
                            path = xMoves + yMoves
                        } else {
                            path = yMoves + xMoves
                        }
                    }
                }
            }
        } else {
            if (current.x == 0) {
                path = xMoves + yMoves
            } else {
                if (end.x == 0) {
                    path = yMoves + xMoves
                } else {
                    if (xMoves.isNotEmpty() && yMoves.isNotEmpty()) {
                        if (xMoves[0] == Move.left) {
                            path = xMoves + yMoves
                        } else {
                            path = yMoves + xMoves
                        }
                    }
                }
            }
        }

        if (path.isEmpty()) {
            path = xMoves + yMoves
        }

        val intPath = path.joinToString("") { it.toIntIdentifier().toString() } + 4
        return intPath

    }

    override fun part2(test: Boolean): Any {
        val inputLines = if (test) inputs.testInput.inputLines else inputs.input.inputLines
        return inputLines.sumOf { s ->
            var l = s.convertUsingNumpad().split(aRegex).groupingBy { it }.eachCount().map { it.key to it.value.toLong() }.toMap()
            repeat(25){
                l = l.convertUsingKeyboard()
            }
            s.filter { it.isDigit() }.toLong() * l.toList().sumOf {
                it.second*it.first.length
            }
        }
    }
}
fun Char.convertToMove():Move{
    return when(this){
        '<' -> Move.left
        '>' -> Move.right
        '^' -> Move.up
        'v' -> Move.down
        else -> throw IllegalArgumentException("Invalid char $this")
    }
}

fun List<String>.coordinatesOf(char: Char): Point2D {
    var point = Point2D(0, 0)
    this.forEachIndexed { y, s ->
        s.forEachIndexed { x, c ->
            if (char == c) {
                point = Point2D(x, y)
            }
        }
    }
    return point
}