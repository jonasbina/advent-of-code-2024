package com.jonasbina.solutions

import com.jonasbina.utils.*

fun main() {
    val input = InputUtils.getDayInputText(20)
    val testInput = InputUtils.getTestInputText(20)
    val inputs = Inputs(input, testInput)
    Day20(inputs).run(correctResultPart1 = 0, correctResultPart2 = 0)
}

class Day20(
    override val inputs:Inputs
): Day(inputs){
    var start = Point2D(0,0)
    var end = Point2D(0,0)
    override fun part1(test:Boolean): Any {
        val inputLines = if(test) inputs.testInput.inputLines else inputs.input.inputLines
        inputLines.forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->
                if (c=='S'){
                    start = Point2D(x,y)
                }
                if (c=='E'){
                    end = Point2D(x,y)
                }
            }
        }
//        aStarFindAllPaths(CurrentPath(start,false,false,null))
        return 0
    }
    class CurrentPath(
        val currentPoint: Point2D,
        val usedCheat:Boolean,
        val cheatFromLastRound:Boolean,
        val lastPoint:Point2D?
    )

    override fun part2(test:Boolean): Any {
        val input = if(test) inputs.testInput else inputs.input
        return 0
    }
}