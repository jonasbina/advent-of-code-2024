package com.jonasbina.solutions

import com.jogamp.newt.event.MouseEvent.PointerType
import com.jonasbina.utils.*
import processing.core.PApplet

fun main() {
    val input = InputUtils.getDayInputText(18)
    val testInput = InputUtils.getTestInputText(18)
    val inputs = Inputs(input, testInput)
    Day18(inputs).run(correctResultPart1 = 0, correctResultPart2 = 0)
    PApplet.main(Day18PApplet().javaClass)
}

class Day18(
    override val inputs: Inputs
) : Day(inputs) {

    override fun part1(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        val barriers = input.inputLines.mapIndexed { i, line ->
            val split = line.trim().split(",").map {
                it.toInt()
            }
            Point2D(split[0], split[1])
        }.take(if (test) 12 else 1024)
        return solveForBarriers(test, barriers)?.cost ?: 0
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

    fun part1Visual(test: Boolean, pApplet: PApplet): Any {
        val input = if (test) inputs.testInput else inputs.input
        val barriers = input.inputLines.mapIndexed { i, line ->
            val split = line.trim().split(",").map {
                it.toInt()
            }
            Point2D(split[0], split[1])
        }.take(if (test) 12 else 1024)
        val start = Point2D(0, 0)
        val end = if (test) Point2D(6, 6) else Point2D(70, 70)
        val size = pApplet.height.toFloat() / (end.x + 1)
        barriers.forEach { bar ->
            pApplet.fill(255)
            pApplet.rect((size * bar.x).toFloat(), (size * bar.y).toFloat(), size.toFloat(), size.toFloat())
        }
        val astarRes = aStarSearch(start, isEnd = { end == it }, next = {
            listOf(it.right(), it.left(), it.up(), it.down()).filter {
                it.isInRange(end.x + 1) && !barriers.contains(it)
            }.map {
                it to 1
            }
        }, { 0 })!!
        astarRes.path.forEach {
            pApplet.fill(255f, 100f, 100f)
            pApplet.rect(
                (it.x * size).toFloat(), (it.y * size).toFloat(), size.toFloat(), size.toFloat()
            )
        }
        return astarRes.cost
    }

    override fun part2(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        val barriers = input.inputLines.mapIndexed { i, line ->
            val split = line.trim().split(",").map {
                it.toInt()
            }
            Point2D(split[0], split[1])
        }
        var size = if (test) 12 else 1024
        var finishingCoord = Point2D(0, 0)
        while (size < barriers.size) {
            val s = solveForBarriers(test, barriers.take(size))
            if (s == null) {
                finishingCoord = barriers.take(size).last()
                break
            }
            size++
        }
        return finishingCoord.x.toString() + "," + finishingCoord.y
    }

    var i = 1024
    fun part2Visual(test: Boolean, pApplet: PApplet) {
        val input = if (test) inputs.testInput else inputs.input
        val barriers = input.inputLines.mapIndexed { i, line ->
            val split = line.trim().split(",").map {
                it.toInt()
            }
            Point2D(split[0], split[1])
        }.take(i)
        val s = solveForBarriers(test, barriers)
        if (s == null){
            solved=true
        }else{
            val end = if (test) Point2D(6, 6) else Point2D(70, 70)
            val size = pApplet.height.toFloat() / (end.x + 1)
            barriers.forEach { bar ->
                pApplet.fill(255)
                pApplet.rect((size * bar.x).toFloat(), (size * bar.y).toFloat(), size.toFloat(), size.toFloat())
            }
            s.path.forEach {
                pApplet.fill(255f, 100f, 100f)
                pApplet.rect(
                    (it.x * size).toFloat(), (it.y * size).toFloat(), size.toFloat(), size.toFloat()
                )
            }
        }
        i++
    }
}
private var solved = false

class Day18PApplet : PApplet() {
    val input = InputUtils.getDayInputText(18)
    val testInput = InputUtils.getTestInputText(18)
    val inputs = Inputs(input, testInput)
    val day = Day18(inputs)
    override fun settings() {
        fullScreen()
    }

    override fun setup() {
        background(0)
    }
    var frames = 0
    override fun draw(){
        if (frames%1==0&&!solved){
            background(0)
            day.part2Visual(false, this)
        }
        frames++
    }
}