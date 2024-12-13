package com.jonasbina.solutions

import com.jonasbina.utils.*

fun main() {
    val input = InputUtils.getDayInputText(12)
    val testInput = InputUtils.getTestInputText(12)
    val inputs = Inputs(input, testInput)
    Day12(inputs).run(correctResultPart1 = 1930L, correctResultPart2 = 1206L)
}

class Day12(
    override val inputs: Inputs
) : Day(inputs) {
    val groups = mutableSetOf<Group>()
    val visited = mutableSetOf<Point2D>()
    override fun part1(test: Boolean): Any {
        groups.clear()
        visited.clear()
        val input = if (test) inputs.testInput else inputs.input
        val inputLines = input.inputLines

        val tp = TestPrintler(test)
        inputLines.forEachIndexed { y, s ->
            s.forEachIndexed inner@{ x, c ->
                val point = Point2D(x, y)
                if (point in visited) return@inner
                val groupPositions = mutableSetOf<Point2D>()
                val stack = ArrayDeque<Point2D>()
                stack.add(point)
                while (stack.isNotEmpty()) {
                    val n = stack.first()
                    stack.removeFirst()

                    //tp.println(n)
                    if (n.isInRange(inputLines.size) && n !in visited) {
                        //tp.println("$n in range and not visited")
                        if (inputLines[n.y][n.x] == c) {
                            //tp.println("$n in group")
                            visited.add(n)
                            groupPositions.add(n)
                            Move.allNonDiagonal().forEach {
                                stack.add(n.applyMove(it))
                            }
                        }
                    }
                }
                groups.add(Group(c, groupPositions))
            }
        }
//        println(
//            groups.joinToString
//            {
//                "${it.name} - S=${it.area}, O=${it.getPerimeter()}"
//            })
        return groups.sumOf {
            it.area * it.getPerimeter()
        }
    }

    override fun part2(test: Boolean): Any {
        val tp = TestPrintler(test)
        val input = if (test) inputs.testInput else inputs.input
        val inputLines = input.inputLines

        return groups.sumOf {
            tp.println(it.getSides().toString() + " " + it.area)
            it.getSides() * it.area
        }
    }

    class Group(
        val name: Char,
        val positions: Set<Point2D>
    ) {
        val area = positions.size
        fun getPerimeter(): Long {
            return positions.sumOf { position ->
                var sides = 0L
                if (position.up() !in positions) sides++
                if (position.down() !in positions) sides++
                if (position.left() !in positions) sides++
                if (position.right() !in positions) sides++
                sides
            }
        }
        fun calculateSides(move: Move):Int{
            val top = positions.filter {
                it.applyMove(move) !in positions
            }
            return top.groupBy {  pointToCoord(it, move).y }.map {
                it.value.sortedBy { pointToCoord(it, move).x }.windowed(2).count {
                    pointToCoord(it[0], move).x+1!= pointToCoord(it[1], move).x
                }+1
            }.sum()
        }
        fun pointToCoord(point2D: Point2D,move:Move):Point2D{
            return when(move){
                Move.up -> point2D
                Move.down -> point2D
                Move.left -> Point2D(point2D.y,point2D.x)
                Move.right -> Point2D(point2D.y,point2D.x)
                else -> point2D
            }
        }
        fun getSides(): Int {
            return Move.allNonDiagonal().sumOf {
                calculateSides(it)
            }
        }
    }
}