package com.jonasbina.solutions

import com.jonasbina.utils.*

fun main() {
    val input = InputUtils.getDayInputText(10)
    val testInput = InputUtils.getTestInputText(10)
    val inputs = Inputs(input, testInput)
    Day10(inputs).run(correctResultPart1 = 1, correctResultPart2 = 81)
}

class Day10(
    override val inputs:Inputs
): Day(inputs){
    
    override fun part1(test:Boolean): Any {
        val input = if(test) inputs.testInput else inputs.input
        val nodes = input.inputLines.mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                c.digitToInt() to Point2D(x,y)
            }
        }
        val size = input.inputLines.size
        val starts = nodes.flatten().filter { it.first==0 }

        return starts.sumOf { startNode->
            aStarSearchReachableGoals(startNode, {it.first==9}, {current->
                Move.allNonDiagonal().map {
                    current.first to current.second.applyMove(it)
                }.filter {
                    it.second.isInRange(size)
                }.map {
                    (nodes[it.second.y][it.second.x]) to 1
                }.filter {
                    it.first.first-current.first==1
                }
            },{1}).map {
                it.path.last().second
            }.toSet().size
        }
    }

    override fun part2(test:Boolean): Any {
        val input = if(test) inputs.testInput else inputs.input
        val nodes = input.inputLines.mapIndexed { y, s ->
            s.mapIndexed { x, c ->
                c.digitToInt() to Point2D(x,y)
            }
        }
        val size = input.inputLines.size
        val starts = nodes.flatten().filter { it.first==0 }

        return starts.sumOf { startNode->
            aStarFindAllPaths(startNode, {it.first==9}, {current->
                Move.allNonDiagonal().map {
                    current.first to current.second.applyMove(it)
                }.filter {
                    it.second.isInRange(size)
                }.map {
                    (nodes[it.second.y][it.second.x]) to 1
                }.filter {
                    it.first.first-current.first==1
                }
            },{1}).size
        }
    }
}