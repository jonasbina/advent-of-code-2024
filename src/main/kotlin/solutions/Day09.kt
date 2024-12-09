package com.jonasbina.solutions

import com.jonasbina.utils.*
import kotlin.math.truncate

fun main() {
    val input = InputUtils.getDayInputText(9)
    val testInput = InputUtils.getTestInputText(9)
    val inputs = Inputs(input, testInput)
    Day09(inputs).run(correctResultPart1 = 1928, correctResultPart2 = 0)
}

class Day09(
    override val inputs: Inputs
) : Day(inputs) {
    var string = mutableListOf<String>()
    override fun part1(test: Boolean): Any {
        val input = (if (test) inputs.testInput else inputs.input).input
        var file = true
        var i = 0
        var id = 0
        string = mutableListOf()
        println("Reading input")
        input.forEach { c ->
            val digit = c.digitToInt()
            if (file) {
                for (x in 0..<digit) {
                    string.add(id.toString())
                }
                id++
            } else {
                for (x in 0..<digit) {
                    string.add(".")
                }
            }
            i += digit
            file = !file
        }
        println("assembling")
        var dots = string.mapIndexed { index, c -> c to index }.filter { it.first.first() == '.' }.map { it.second }
            .toMutableList()
        var numbers = string.mapIndexed { index, c -> c to index }.filter { it.first.first().isDigit() }.reversed()
            .toMutableList()
        while (true) {
            val d = dots.first()
            val n = numbers.first()
            if (n.second < d) {
                break
            } else {
                numbers.remove(n)
                numbers.add(n.first to d)
                dots.remove(d)
                dots.add(n.second)
                dots.sort()
                numbers.sortByDescending {
                    it.second
                }
                string[d] = n.first
                string[n.second] = "."
            }
        }
        println("calculating")
        var sum = 0L
        string.filter { it.first().isDigit() }.forEachIndexed { ind, c ->
            sum += c.toInt() * ind
        }
        return sum
    }

    override fun part2(test: Boolean): Any {
        val input = (if (test) inputs.testInput else inputs.input).input
        val clusters = mutableListOf<List<Pair<String, Int>>>()
        var cluster = mutableListOf<Pair<String, Int>>()
        var file = true
        var i = 0
        var id = 0
        string = mutableListOf()
        println("Reading input")
        input.forEach { c ->
            val digit = c.digitToInt()
            if (file) {
                for (x in 0..<digit) {
                    string.add(id.toString())
                }
                id++
            } else {
                for (x in 0..<digit) {
                    string.add(".")
                }
            }
            i += digit
            file = !file
        }

        string.forEachIndexed { i, it ->
            if (cluster.isEmpty()) {
                cluster.add(it to i)
            } else {
                if (cluster[0].first != it) {
                    clusters.add(cluster)
                    cluster = mutableListOf(it to i)
                } else {
                    cluster.add(it to i)
                }
            }
        }
        var dotClusters =
            clusters.mapIndexed { index, c -> c to index }.filter { it.first.any { it.first.first() == '.' } }
                .toMutableList()
        var numberClusters =
            clusters.mapIndexed { index, c -> c to index }.filter { it.first.any { it.first.first().isDigit() } }
                .reversed().toMutableList()

        while (true) {
            val d = dotClusters.first()
            if (numberClusters.none { ncluster -> ncluster.first.joinToString("") { it.first }.length <= d.first.size }) {
                break
            } else {
                val n = numberClusters.first { ncluster -> ncluster.first.joinToString("") { it.first }.length <= d.first.size }


                numberClusters.remove(n)
                numberClusters.add(n.first to d.second)
                dotClusters.remove(d)
                dotClusters.add(d.first to n.second)
                dotClusters.sortBy {
                    it.second
                }
                numberClusters.sortByDescending {
                    it.second
                }
                val dots = d.first
                testPrintln(string.toString(),test)
                n.first.forEachIndexed { index, c ->
                    val dot = dots[index]
                    string[dot.second]=c.first
                    string[c.second]="."
                }
                testPrintln(string.toString(),test)
            }
        }
        println("calculating")
        var sum = 0L
        testPrintln(string.toString(), test)
        string.filter { it.first().isDigit() }.forEachIndexed { ind, c ->
            sum += c.toInt() * ind
        }
        return sum
    }
}