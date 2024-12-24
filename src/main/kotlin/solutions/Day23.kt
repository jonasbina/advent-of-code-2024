package com.jonasbina.solutions

import com.jonasbina.utils.Day
import com.jonasbina.utils.InputUtils
import com.jonasbina.utils.Inputs

fun main() {
    val input = InputUtils.getDayInputText(23)
    val testInput = InputUtils.getTestInputText(23)
    val inputs = Inputs(input, testInput)
    Day23(inputs).run(correctResultPart1 = 0, correctResultPart2 = 0)
}

class Day23(
    override val inputs: Inputs
) : Day(inputs) {

    override fun part1(test: Boolean): Any {
        val inputLines = if (test) inputs.testInput.inputLines else inputs.input.inputLines
        val map = mutableMapOf<String, MutableSet<String>>()
        inputLines.forEach { l ->
            val (comp1, comp2) = l.split("-")
            map.getOrPut(comp1) { mutableSetOf() }.add(comp2)
            map.getOrPut(comp2) { mutableSetOf() }.add(comp1)
        }
        val triples = mutableSetOf<Set<String>>()
        for (comp1 in map.keys) {
            for (comp2 in map[comp1]!!) {
                for (comp3 in map[comp2]!!) {
                    if (comp1 != comp3 && map[comp1]?.contains(comp3) == true) {
                        val triple = setOf(comp1, comp2, comp3)
                        if (triple.any { it.startsWith('t') }) {
                            triples.add(triple)
                        }
                    }
                }
            }
        }
        return triples.size
    }

    override fun part2(test: Boolean): Any {
        val inputLines = if (test) inputs.testInput.inputLines else inputs.input.inputLines
        val map = mutableMapOf<String, MutableSet<String>>()
        inputLines.forEach { l ->
            val (comp1, comp2) = l.split("-")
            map.getOrPut(comp1) { mutableSetOf() }.add(comp2)
            map.getOrPut(comp2) { mutableSetOf() }.add(comp1)
        }
        var largestGroup = setOf<String>()
        map.forEach {(comp, connected)->
            val currentGroup = mutableSetOf(comp)
            connected.forEach { comp1 ->
                var add = true
                val connected1 = map[comp1]!!
                currentGroup.forEach { groupComp->
                    if (groupComp !in connected1 &&groupComp!=comp1) {
                        add=false
                    }
                }
                if (add){
                    currentGroup.add(comp1)
                }
            }
            if (currentGroup.size>largestGroup.size){
                largestGroup = currentGroup
            }
        }
        return largestGroup.sortedBy { it }.joinToString(",")
    }
}