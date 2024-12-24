package com.jonasbina.solutions

import com.jonasbina.utils.*

fun main() {
    val input = InputUtils.getDayInputText(20)
    val testInput = InputUtils.getTestInputText(20)
    val inputs = Inputs(input, testInput)
    Day20(inputs).run(correctResultPart1 = 0, correctResultPart2 = 0)
}

class Day20(
    override val inputs: Inputs
) : Day(inputs) {
    var start = Point2D(0, 0)
    var end = Point2D(0, 0)
    override fun part1(test: Boolean): Any {
        val inputLines = if (test) inputs.testInput.inputLines else inputs.input.inputLines
        println("loading START and END positions")
        inputLines.forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->
                if (c == 'S') {
                    start = Point2D(x, y)
                }
                if (c == 'E') {
                    end = Point2D(x, y)
                }
            }
        }
        println("finding best path without cheating")
        val noCheatBest = aStarSearch(start, isEnd = { it == end }, {
            it.neighbors().filter {
                it.isInRange(inputLines.size) && inputLines[it.y][it.x] != '#'
            }.map {
                it to 1
            }
        }, { 0 })!!.cost
        println(noCheatBest)
        println("finding all possible cheats")
        val possibleCheats = inputLines.mapIndexed { y, s ->
            s.mapIndexedNotNull { x, c ->
                if (c == '#') {
                    val p = Point2D(x, y)

                    if (p.neighbors().filter { it.getInput(inputLines) != '#' }.size >= 2
                    ) {
                        p
                    } else {
                        null
                    }
                } else {
                    null
                }
            }
        }.flatten().toMutableList()
        println("mapping all possible cheats")
        println("Possible cheats: ${possibleCheats.size}")
        val cheats = possibleCheats.mapIndexed { i, cheat ->
            val diff = noCheatBest - aStarSearch(start, isEnd = { it == end }, { p ->
                p.neighbors().filter {
                    (it.isInRange(inputLines.size) && inputLines[it.y][it.x] != '#') || it == cheat
                }.map {
                    it to 1
                }
            }, { it.distanceTo(end) })!!.cost
            diff
        }.filter { it>=100 }
        println(cheats.sortedDescending().groupingBy { it }.eachCount().map {
            "${it.key} - ${it.value}"
        }.joinToString("\n"))
        return cheats.size
    }

    class CurrentPath(
        val currentPoint: Point2D,
        val usedCheat: Boolean,
        val cheatFromLastRound: Boolean,
        val lastPoint: Point2D?
    )

    override fun part2(test: Boolean): Any {
        val inputLines = if (test) inputs.testInput.inputLines else inputs.input.inputLines
        val emptySpaces = inputLines.mapIndexed { y, s ->
            s.mapIndexedNotNull { x, c ->
                if (c != '#') {
                    Point2D(x, y)
                } else {
                    null
                }
            }
        }.flatten()


        val noCheatBest = aStarSearch(start, isEnd = { it == end }, {
            it.neighbors().filter {
                it.isInRange(inputLines.size) && inputLines[it.y][it.x] != '#'
            }.map {
                it to 1
            }
        }, { 0 })!!.cost
        println(noCheatBest)
        println("Finding start spaces")
        val startSpaces = emptySpaces.map { p ->
            p to (aStarSearch(start, isEnd = {n->n==p}, next = {n->n.neighbors().filter {n->  n.isInRange(inputLines.size) && inputLines[n.y][n.x] != '#' }.map {n->
                n to 1
            }}, heuristicCostToEnd = {it.distanceTo(p)})?.cost?:Int.MAX_VALUE)
        }.filter {
            noCheatBest-100>=it.second
        }
        println(startSpaces.size)
        println("Finding end spaces")
        val endSpaces = emptySpaces.map { p ->
            p to (aStarSearch(p, isEnd = {n->n==end}, next = {n->n.neighbors().filter {v->  v.isInRange(inputLines.size) && inputLines[v.y][v.x] != '#' }.map {z->
                z to 1
            }}, heuristicCostToEnd = {it.distanceTo(end)})?.cost?:Int.MAX_VALUE)
        }.filter {
            noCheatBest-100>=it.second
        }
        println(endSpaces.size)
        println("Mapping")
        return startSpaces.map {(pos, cost)->
            endSpaces.filter { (p,cost2) -> p.distanceTo(pos) <= 20&&noCheatBest-100>=cost2+cost+pos.distanceTo(p)}
        }.flatten().size
    }
}