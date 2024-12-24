package com.jonasbina.solutions

import com.jonasbina.utils.*

fun main() {
    val input = InputUtils.getDayInputText(21)
    val testInput = InputUtils.getTestInputText(21)
    val inputs = Inputs(input, testInput)
    Day21(inputs).run(correctResultPart1 = 0, correctResultPart2 = 0)
}

class Day21(
    override val inputs: Inputs
) : Day(inputs) {
    val numericKeyboard = listOf(
        "789",
        "456",
        "123",
        " 0A"
    )
    val arrowKeyboard = listOf(
        " ^A",
        "<v>"
    )

    override fun part1(test: Boolean): Any {
        val inputLines = if (test) inputs.testInput.inputLines else inputs.input.inputLines
        return inputLines.sumOf {
            val l = listOf(it).convertUsingKeyboard(numericKeyboard)
                .convertUsingKeyboard(arrowKeyboard)
                .convertUsingKeyboard(arrowKeyboard)
            val str = l.minBy { it.length }
            val x = str.length
            println(str)
            it.filter { it.isDigit() }.toInt() * x
        }
    }


    fun List<String>.convertUsingKeyboard(keyboard: List<String>): List<String> {

        val start = keyboard.coordinatesOf('A')

        val listList=this.map {combination->
            var cur = start
            val list = combination.map {c->
                val end = keyboard.coordinatesOf(c)
                val r = findAllPaths(keyboard,cur,end)
                cur = end
                r
            }
            generateCombinations(list.map { it.toList() })
        }
        return generateCombinations(listList)
    }
    val cache = mutableMapOf<Pair<Int, String>, List<String>>()

    fun generateCombinations(lists: List<List<String>>): List<String> {
        if (lists.size == 1) return lists[0]
        println("launched")
        val indexes = Array(lists.size){0}
        val list = mutableSetOf<String>()
        outer@while (true){
            list.addAll(lists.mapIndexed { index, strings ->
                strings[indexes[index]]
            })
            indexes[indexes.lastIndex]=indexes[indexes.lastIndex]+1
            for(index in lists.indices.reversed()){
                val it = indexes[index]
                if (it>lists[index].lastIndex){
                    if (index==0){
                        break@outer
                    }
                    indexes[index]=0
                    indexes[index-1]++
                }
            }
        }
        println("done $list")
        return list.toList()
    }
    fun findAllPaths(keyboard: List<String>, current: Point2D, end: Point2D): Set<String> {
        val gap = keyboard.coordinatesOf(' ')
        val xMoves = mutableSetOf<Move>()
        val yMoves = mutableSetOf<Move>()
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
        var paths = setOf(yMoves + xMoves, xMoves + yMoves).filter {
            var c = current
            it.forEach { m ->
                c = c.applyMove(m)
                if (c == gap) return@filter false
            }
            true
        }.toSet()
        if (paths.isEmpty()){
            val c:Pair<Point2D,Move?> = current to null
            paths = setOf(aStarSearch(c,{c.first==end}, next = {Move.allNonDiagonal().map { move-> (it.first.applyMove(move) to move) to 1 }.filter { it.first.first!=gap }},{it.first.distanceTo(end)})!!.path.drop(1).map {
                it.second!!
            }.toSet())
        }

        return paths.map {
            it.map { it.toArrow() }
                .joinToString("") + "A"
        }.toSet()

    }

    override fun part2(test: Boolean): Any {
        val inputLines = if (test) inputs.testInput.inputLines else inputs.input.inputLines
        return 0
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