package com.jonasbina.solutions

import com.jonasbina.utils.*
import kotlin.math.abs

fun main() {
    val input = InputUtils.getDayInputText(8)
    val testInput = InputUtils.getTestInputText(8)
    val inputs = Inputs(input, testInput)
    Day08(inputs).run(correctResultPart1 = 14, correctResultPart2 = 34)
}

class Day08(
    override val inputs: Inputs
) : Day(inputs) {
    lateinit var antenna: Map<Char, List<Antenna>>
    override fun part1(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        antenna = input.inputLines.mapIndexed { index, s ->
            s.mapIndexed { i, c -> Antenna(i, index, c) }.filter { it.frequency != '.' }
        }.flatten().groupBy { it.frequency }

        val antinodes = mutableSetOf<Point2D>()
        antenna.forEach { t, u ->
            u.forEach { a1 ->

                (antenna[t]!! - a1).forEach { a2 ->
                    val a1pos = Point2D(a1.x, a1.y)
                    val a2pos = Point2D(a2.x, a2.y)
                    val distX = abs(a1pos.x - a2pos.x)
                    val distY = abs(a1pos.y - a2pos.y)
                    var first = a1pos
                    var second = a2pos
                    if (a1pos.x > a2pos.x) {
                        first = first.applyMove(Move(distX, 0))
                        second = second.applyMove(Move(-distX, 0))
                    } else {
                        first = first.applyMove(Move(-distX, 0))
                        second = second.applyMove(Move(distX, 0))
                    }
                    if (a1pos.y > a2pos.y) {
                        first = first.applyMove(Move(0, distY))
                        second = second.applyMove(Move(0, -distY))
                    } else {
                        first = first.applyMove(Move(0, -distY))
                        second = second.applyMove(Move(0, distY))
                    }
                    if (first.isInRange(input.inputLines.size)) {
                        antinodes.add(first)
                    }
                    if (second.isInRange(input.inputLines.size)) {
                        antinodes.add(second)
                    }

                }
            }
        }
        return antinodes.size
    }

    override fun part2(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        antenna = input.inputLines.mapIndexed { index, s ->
            s.mapIndexed { i, c -> Antenna(i, index, c) }.filter { it.frequency != '.' }
        }.flatten().groupBy { it.frequency }

        val antinodes = mutableSetOf<Point2D>()
        antenna.forEach { t, u ->
            u.forEach { a1 ->

                (antenna[t]!! - a1).forEach { a2 ->
                    val a1pos = Point2D(a1.x, a1.y)
                    val a2pos = Point2D(a2.x, a2.y)
                    antinodes.add(a1pos)
                    antinodes.add(a2pos)
                    val distX = abs(a1pos.x - a2pos.x)
                    val distY = abs(a1pos.y - a2pos.y)
                    var first = a1pos
                    var second = a2pos
                    while (first.isInRange(input.inputLines.size) || second.isInRange(input.inputLines.size)) {
                        if (a1pos.x > a2pos.x) {
                            first = first.applyMove(Move(distX, 0))
                            second = second.applyMove(Move(-distX, 0))
                        } else {
                            first = first.applyMove(Move(-distX, 0))
                            second = second.applyMove(Move(distX, 0))
                        }
                        if (a1pos.y > a2pos.y) {
                            first = first.applyMove(Move(0, distY))
                            second = second.applyMove(Move(0, -distY))
                        } else {
                            first = first.applyMove(Move(0, -distY))
                            second = second.applyMove(Move(0, distY))
                        }
                        if (first.isInRange(input.inputLines.size)) {
                            antinodes.add(first)
                        }
                        if (second.isInRange(input.inputLines.size)) {
                            antinodes.add(second)
                        }
                    }
                }
            }
        }
        return antinodes.size
    }

    data class Antenna(
        val x: Int,
        val y: Int,
        val frequency: Char
    )
}