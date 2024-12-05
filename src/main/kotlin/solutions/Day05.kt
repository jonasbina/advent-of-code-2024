package com.jonasbina.solutions

import com.jonasbina.utils.Day
import com.jonasbina.utils.InputUtils
import com.jonasbina.utils.Inputs

fun main() {
    val input = InputUtils.getDayInputText(5)
    val testInput = InputUtils.getTestInputText(5)
    val inputs = Inputs(input, testInput)
    Day05(inputs).run(correctResultPart1 = 143, correctResultPart2 = 123)
}

private class Rule(val smaller: List<Int>, val bigger: List<Int>)

class Day05(
    override val inputs: Inputs
) : Day(inputs) {
    private lateinit var rules: HashMap<Int, Rule>
    lateinit var updates: List<List<Int>>
    lateinit var mappedByValidity: List<Pair<Boolean,List<Pair<Int,Int>>>>
    override fun part1(test: Boolean): Any {
        val input = if (test) inputs.testInput else inputs.input
        rules = hashMapOf()
        val sections = input.input.split("\n\n")
        val rulesStrings = sections.first().split("\n")
        val updatesString = sections.last().split("\n")
        updates = updatesString.map {
            it.split(",").map { it.toInt() }
        }
        rulesStrings.forEach {
            parseRule(it)
        }
        mappedByValidity = updates.map {
            it.mapIndexed { index, i ->
                i to index
            }
        }.map { list ->
            isUpdateValid(list) to list
        }
        return mappedByValidity.filter { it.first }.sumOf {
            it.second[it.second.lastIndex/2].first
        }
    }
    fun isUpdateValid(list:List<Pair<Int,Int>>):Boolean{
        return list.all { entry ->
            isNumberCorrect(list,entry)
        }
    }
    fun isNumberCorrect(list: List<Pair<Int,Int>>, entry:Pair<Int,Int>):Boolean{
        val number = entry.first
        val index = entry.second
        val rule = rules[number] ?: Rule(emptyList(), emptyList())
        return list.take(index).none { rule.bigger.contains(it.first) } && list.takeLast(list.lastIndex - index).none { rule.smaller.contains(it.first) }
    }

    override fun part2(test: Boolean): Any {
        return mappedByValidity.filter { !it.first }.map { it.second.map { it.first } }.map {
            it.sortedWith { a, b ->
                when {
                    rules[a]?.bigger?.contains(b) == true -> -1
                    rules[a]?.smaller?.contains(b) == true -> 1
                    else -> 0
                }
            }
        }.sumOf { it[it.lastIndex/2] }
    }

    fun parseRule(rule: String) {
        val split = rule.split("|")
        val smaller = split.first().toInt()
        val bigger = split.last().toInt()
        if (rules.contains(smaller)) {
            val current = rules[smaller]!!
            rules[smaller] = Rule(current.smaller, current.bigger + bigger)
        } else {
            rules[smaller] = Rule(emptyList(), listOf(bigger))
        }
        if (rules.contains(bigger)) {
            val current = rules[bigger]!!
            rules[bigger] = Rule(current.smaller + smaller, current.bigger)
        } else {
            rules[bigger] = Rule(listOf(smaller), emptyList())
        }
    }
}