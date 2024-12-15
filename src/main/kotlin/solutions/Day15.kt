package com.jonasbina.solutions

import com.jonasbina.utils.*

fun main() {
    val input = InputUtils.getDayInputText(15)
    val testInput = InputUtils.getTestInputText(15)
    val inputs = Inputs(input, testInput)
    Day15(inputs).run(correctResultPart1 = 10092L, correctResultPart2 = 9021)
}

class Day15(
    override val inputs: Inputs
) : Day(inputs) {
    lateinit var tp: TestPrintler
    var robotInitPosition = Point2D(0, 0)
    override fun part1(test: Boolean): Any {
        val input = if (test) inputs.testInput.input else inputs.input.input
        tp = TestPrintler(test)
        var (map, moves) = parseInput(input)
        map = map.map { it.toMutableList() }.toMutableList()
        var current = robotInitPosition
        moves.forEach { move ->
            val nextPos = current.applyMove(move)
            val (nx, ny) = nextPos
            val pointInMap = map[ny][nx]
            if (pointInMap != Space.Wall) {
                if (pointInMap == Space.Empty) {
                    map[current.y][current.x] = Space.Empty
                    current = nextPos
                } else {
                    var npos = nextPos
                    var possible = false
                    var amountOfBoxes = 0
                    while (!possible) {
                        val npInMap = map[npos.y][npos.x]
                        if (npInMap == Space.Wall) {
                            break
                        }
                        if (npInMap == Space.Empty) {
                            possible = true
                        }
                        if (npInMap == Space.Box) {
                            npos = npos.applyMove(move)
                            amountOfBoxes++
                        }
                    }
                    if (possible) {
                        map[current.y][current.x] = Space.Empty
                        map[ny][nx] = Space.Empty
                        current = nextPos
                        var curPos = nextPos

                        repeat(amountOfBoxes) {
                            curPos = curPos.applyMove(move)
                            map[curPos.y][curPos.x] = Space.Box
                        }
                    }
                }
            }
        }
        return map.mapIndexed { y, spaces ->
            spaces.mapIndexed { x, space ->
                if (space != Space.Box) {
                    0L
                } else {
                    y * 100L + x
                }
            }
        }.flatten().sum()
    }


    override fun part2(test: Boolean): Any {
        val input = if (test) inputs.testInput.input else inputs.input.input
        var (map, moves) = parseInputPart2(input)
        map = map.map { it.toMutableList() }.toMutableList()
        var current = robotInitPosition
        moves.forEach { move ->
            val nextPos = current.applyMove(move)
            val (nx, ny) = nextPos
            val pointInMap = map[ny][nx]
            if (pointInMap.space != Space.Wall) {
                if (pointInMap.space == Space.Empty) {
                    map[current.y][current.x] = BetterSpace(Space.Empty, emptyPoint)
                    current = nextPos
                } else {
                    var boxes = setOf(nextPos to pointInMap.otherBox!!, pointInMap.otherBox!! to nextPos)
                    var foundWall = false
                    while (!foundWall) {
                        val newBoxes = boxes.toMutableSet()
                        boxes.forEach { box ->
                            val newPos = box.first.applyMove(move)
                            val newBox = map[newPos.y][newPos.x]
                            if (newBox.space == Space.Box) {
                                newBoxes.add(newBox.otherBox!! to newPos)
                                newBoxes.add(newPos to newBox.otherBox!!)
                            } else {
                                if (newBox.space == Space.Wall) {
                                    foundWall = true
                                }
                            }
                        }
                        if (boxes == newBoxes) {
                            break
                        }
                        boxes = newBoxes
                    }
                    if (!foundWall) {
                        current = nextPos
                        val prevToCurBoxes = boxes.map {
                            val prevBox = BetterSpace(Space.Box, it.second)
                            val nextBox = BetterSpace(Space.Box, it.second.applyMove(move))
                            (prevBox to it.first) to (nextBox to it.first.applyMove(move))
                        }
                        //this single .reversed() took me 1 hour to figure out :)
                        prevToCurBoxes.reversed().forEach {
                            val prevPos = it.first.second
                            val nexPos = it.second.second
                            val inMap = map[prevPos.y][prevPos.x]
                            if (inMap == it.first.first) {
                                map[prevPos.y][prevPos.x] = BetterSpace(Space.Empty, emptyPoint)
                            }
                            map[nexPos.y][nexPos.x] = it.second.first
                        }
                    }

                }
            }
        }
        val dontDo = mutableSetOf<Point2D>()
        return map.mapIndexed { y, spaces ->
            spaces.mapIndexed { x, space ->
                if (Point2D(x, y) !in dontDo) {
                    if (space.space != Space.Box) {
                        0L
                    } else {
                        dontDo.add(space.otherBox)
                        y * 100L + x
                    }
                } else {
                    0L
                }
            }
        }.flatten().sum()
    }

    fun mapToString(map: List<List<BetterSpace>>, current: Point2D): String {
        return map.mapIndexed { y, spaces ->
            spaces.mapIndexed { x, space ->
                if (Point2D(x, y) == current) {
                    '@'
                } else {
                    when (space.space) {
                        Space.Empty -> '.'
                        Space.Box -> 'O'
                        Space.Wall -> '#'
                    }
                }
            }.joinToString("")
        }.joinToString("\n")
    }

    enum class Space(val c: Char) {
        Empty('.'),
        Wall('#'),
        Box('O');
    }

    fun parseInput(input: String): Pair<List<List<Space>>, List<Move>> {
        val split = input.split("\n\n")
        return split[0].split("\n").mapIndexed { y, s ->
            s.mapIndexed { x, it ->
                var c = it
                if (c == '@') {
                    robotInitPosition = Point2D(x, y)
                    c = '.'
                }
                when (c) {
                    '#' -> Space.Wall
                    'O' -> Space.Box
                    '.' -> Space.Empty
                    else -> Space.Empty
                }
            }
        } to split[1].filter { it != '\n' }.trim().map {
            when (it) {
                '^' -> Move.up
                'v' -> Move.down
                '<' -> Move.left
                '>' -> Move.right
                else -> Move(0, 0)
            }
        }
    }

    data class BetterSpace(
        val space: Space,
        val otherBox: Point2D,
    )

    val emptyPoint = Point2D(0, 0)
    fun parseInputPart2(input: String): Pair<List<List<BetterSpace>>, List<Move>> {
        val split = input.split("\n\n")
        return split[0].split("\n").mapIndexed { y, s ->
            s.mapIndexed { x, it ->
                val actualX = x * 2
                var c = it
                if (c == '@') {
                    robotInitPosition = Point2D(actualX, y)
                    c = '.'
                }
                val otherBox = Point2D(actualX + 1, y)
                val otherBox2 = Point2D(actualX, y)
                when (c) {
                    '#' -> listOf(BetterSpace(Space.Wall, emptyPoint), BetterSpace(Space.Wall, emptyPoint))
                    'O' -> listOf(BetterSpace(Space.Box, otherBox), BetterSpace(Space.Box, otherBox2))
                    '.' -> listOf(BetterSpace(Space.Empty, emptyPoint), BetterSpace(Space.Empty, emptyPoint))
                    else -> listOf(BetterSpace(Space.Empty, emptyPoint), BetterSpace(Space.Empty, emptyPoint))
                }
            }.flatten()
        } to split[1].filter { it != '\n' }.trim().map {
            when (it) {
                '^' -> Move.up
                'v' -> Move.down
                '<' -> Move.left
                '>' -> Move.right
                else -> Move(0, 0)
            }
        }
    }
}