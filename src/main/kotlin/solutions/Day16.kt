package com.jonasbina.solutions

import com.jonasbina.utils.*

fun main() {
    val input = InputUtils.getDayInputText(16)
    val testInput = InputUtils.getTestInputText(16)
    val inputs = Inputs(input, testInput)
    Day16(inputs).run(correctResultPart1 = 11048, correctResultPart2 = 64)
}

class Day16(
    override val inputs:Inputs
): Day(inputs){
    lateinit var startPosition:Position
    lateinit var endPoint:Point2D
    override fun part1(test:Boolean): Any {
        val inputLines = if(test) inputs.testInput.inputLines else inputs.input.inputLines
        getStartAndEndPositions(inputLines)
        return aStarSearch(start = startPosition, isEnd = {it.point==endPoint},{
            val p = it.point
            val toTheRight = it.facing.turn90Deg()
            val toTheLeft = it.facing.turn90Deg().turn90Deg().turn90Deg()
            listOf(Position(p.applyMove(it.facing),it.facing) to 1, Position(p.applyMove(toTheLeft),toTheLeft) to 1001, Position(p.applyMove(toTheRight),toTheRight) to 1001).filter {
                val point = it.first.point
                point.isInRange(inputLines.size) && inputLines[point.y][point.x] != '#'
            }
        },{
            0
        })?.cost?:0
    }
    fun getStartAndEndPositions(inputLines:List<String>){
        inputLines.forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->
                if (c=='E'){
                    endPoint = Point2D(x, y)
                }
                if (c=='S'){
                    startPosition = Position(Point2D(x,y), Move.right) //facing east
                }
            }
        }
    }


    override fun part2(test:Boolean): Any {
        val inputLines = if(test) inputs.testInput.inputLines else inputs.input.inputLines
        return aStarSearchAllOptimalPaths(start = startPosition, isEnd = {it.point==endPoint},{
            val p = it.point
            val toTheRight = it.facing.turn90Deg()
            val toTheLeft = it.facing.turn90Deg().turn90Deg().turn90Deg()
            listOf(Position(p.applyMove(it.facing),it.facing) to 1, Position(p.applyMove(toTheLeft),toTheLeft) to 1001, Position(p.applyMove(toTheRight),toTheRight) to 1001).filter {
                val point = it.first.point
                point.isInRange(inputLines.size) && inputLines[point.y][point.x] != '#'
            }
        },{
            0
        }).map {
            it.path.flatten().map { it.point }
        }.flatten().toSet().size
    }
    data class Position(val point:Point2D, val facing:Move)
}