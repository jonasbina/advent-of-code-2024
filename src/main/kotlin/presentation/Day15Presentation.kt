package com.jonasbina.presentation

import com.jonasbina.solutions.Day15.BetterSpace
import com.jonasbina.solutions.Day15.Space
import com.jonasbina.utils.InputUtils
import com.jonasbina.utils.Move
import com.jonasbina.utils.Point2D
import processing.core.PApplet

class Day15Presentation :PApplet(){

    lateinit var map:MutableList<MutableList<BetterSpace>>
    lateinit var moves: List<Move>
    lateinit var current:Point2D
    var i = 0
    val emptyPoint = Point2D(0, 0)
    var started = false
    lateinit var robotInitPosition:Point2D
    fun parseInputPart2(input:String): Pair<List<List<BetterSpace>>, List<Move>> {
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
    fun applyMove(move: Move){
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
    override fun settings() {
        val input = InputUtils.getTestInputText(15).input
        val parsed = parseInputPart2(input)
        map = parsed.first.map { it.toMutableList() }.toMutableList()
        moves = parsed.second
        current = robotInitPosition
        val ratio = map[0].size.toFloat()/map.size
        size((ratio*920).toInt(),(920).toInt())
        println()
    }

    override fun mouseClicked() {
        started = true
    }
    override fun setup() {
        background(0)
        drawGrid()
    }
    var frames = 0
    var moveIndex = 0
    override fun draw() {
        frames++
        if (frames%1==0&&moveIndex<=moves.lastIndex&&started){
            //tick
            background(0)
            val move = moves[moveIndex]
            applyMove(move)
            drawGrid()
            moveIndex++
        }
    }
    fun drawGrid(){
        val w = width.toFloat()/map[0].size
        val h = height.toFloat()/map.size
        map.forEachIndexed { y, betterSpaces ->
            betterSpaces.forEachIndexed { x, betterSpace ->
                var color = when(betterSpace.space){
                    Space.Box -> color(100,100,100)
                    Space.Wall -> color(255,255,255)
                    Space.Empty -> color(0,0,0)
                }
                if (Point2D(x,y)==current){
                    color=color(255,0,0)
                }
                fill(color)
                rect(w*x,h*y,w/1.05f,h/1.05f)
            }
        }
    }
}
fun main(){
    PApplet.main(Day15Presentation().javaClass)
}