package com.jonasbina.presentation

import com.jonasbina.solutions.Day18
import com.jonasbina.utils.*
import processing.core.PApplet

private var solved = false

class Day18Presentation : PApplet() {
    val input = InputUtils.getDayInputText(18)
    val testInput = InputUtils.getTestInputText(18)
    val inputs = Inputs(input, testInput)
    var amountOfPathDisplayed = 0
    var part1 = true
    lateinit var path:List<Point2D>
    override fun settings() {
        fullScreen()
    }

    override fun setup() {
        background(0)
        frameRate(320f)
    }
    var frames = 0
    override fun draw(){
        if (frames%1==0&&!solved){
            background(0)
            part1Visual(false)
        }
        frames++
    }
    override fun keyPressed() {
        if (key=='1'){
            part1 = true
        }
        if (key=='2'){
            part1 = false
        }
    }
    fun solveForBarriers(test: Boolean, barriers: List<Point2D>): AStarResult<Point2D>? {
        val start = Point2D(0, 0)
        val end = if (test) Point2D(6, 6) else Point2D(70, 70)
        return aStarSearch(start, isEnd = { end == it }, next = {
            listOf(it.right(), it.left(), it.up(), it.down()).filter { x ->
                val contains = barriers.contains(x)
                val inRange = x.isInRange(end.x + 1)
                inRange && !contains
            }.map { aaa ->
                aaa to 1
            }
        }, { 0 })
    }
    var i = 12
    fun part2Visual(test: Boolean) {
        val input = if (test) inputs.testInput else inputs.input
        val barriersAll = input.inputLines.mapIndexed { i, line ->
            val split = line.trim().split(",").map {
                it.toInt()
            }
            Point2D(split[0], split[1])
        }
        val barriers = barriersAll.take(i)
        val s = solveForBarriers(test, barriers)
        val end = if (test) Point2D(6, 6) else Point2D(70, 70)
        val size = height.toFloat() / (end.x + 1)
        barriers.forEach { bar ->
            fill(255)
            rect((size * bar.x), (size * bar.y), size, size)
        }
        if (s == null){
            solved =true
        }else{
            s.path.forEach {
                fill(255f, 100f, 100f)
                rect(
                    (it.x * size), (it.y * size), size, size
                )
            }
        }
        i = barriersAll.indexOfFirst {
            it in (s?.path ?: listOf())
        }+1
    }
    fun part1Visual(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        val barriers = input.inputLines.mapIndexed { i, line ->
            val split = line.trim().split(",").map {
                it.toInt()
            }
            Point2D(split[0], split[1])
        }.take(if (test) 12 else 1024)
        val start = Point2D(0, 0)
        val end = if (test) Point2D(6, 6) else Point2D(70, 70)
        val size = height.toFloat() / (end.x + 1)
        barriers.forEach { bar ->
            fill(255)
            rect((size * bar.x), (size * bar.y), size, size)
        }
        val astarRes = aStarSearch(start, isEnd = { end == it }, next = {
            listOf(it.right(), it.left(), it.up(), it.down()).filter {
                it.isInRange(end.x + 1) && !barriers.contains(it)
            }.map {
                it to 1
            }
        }, { 0 })!!
        astarRes.path.forEach {
            fill(255f, 100f, 100f)
            rect(
                (it.x * size), (it.y * size), size, size
            )
        }
        return astarRes.cost
    }
}

fun main(){
    PApplet.main(Day18Presentation().javaClass)
}