package com.jonasbina.solutions

import com.jonasbina.utils.*
import kotlin.math.truncate

fun main() {
    val input = InputUtils.getDayInputText(9)
    val testInput = InputUtils.getTestInputText(9)
    val inputs = Inputs(input, testInput)
    Day09(inputs).run(correctResultPart1 = 1928, correctResultPart2 = 2858)
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
        var clusters = calculateClusters()
        var numberClusters = clusters.second
        var dotClusters = clusters.first
        testPrintln(numberClusters.toString(), test)
        var previous = emptyList<String>()
        while (true) {
            dotClusters.forEach { d ->
                // i hope that files with multiple digit indexes count as a single dot
                val n = numberClusters.filter { ncluster ->
                    ncluster.first.size <= d.first.size && ncluster.first.first().second > d.first.first().second
                }.firstOrNull()
                if (n != null) {

                    val dots = d.first
                    //testPrintln(string.toString(),test)
                    n.first.forEachIndexed { index, c ->
                        val dot = dots[index]
                        testPrintln(string, test)
                        string[dot.second] = c.first
                        string[c.second] = "."
                        testPrintln(string, test)
                    }
                    clusters = calculateClusters()
                    numberClusters = clusters.second
                    dotClusters = clusters.first
                }
            }

            if (previous == string) {
                break
            }
            previous = string
        }
        println("calculating")
        var sum = 0L
        testPrintln(string.toString(), test)
        string.filter { it.first().isDigit() }.forEachIndexed { ind, c ->
            sum += c.toInt() * ind
        }
        return sum
    }

    fun calculateClusters(): Pair<MutableList<Pair<List<Pair<String, Int>>, Int>>, MutableList<Pair<List<Pair<String, Int>>, Int>>> {
        val clusters = mutableListOf<List<Pair<String, Int>>>()
        var cluster = mutableListOf<Pair<String, Int>>()

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
        if (cluster.isNotEmpty()) {
            clusters.add(cluster)
        }
        return clusters.mapIndexed { index, c -> c to index }.filter { it.first.any { it.first.first() == '.' } }
            .toMutableList() to clusters.mapIndexed { index, c -> c to index }
            .filter { it.first.any { it.first.first().isDigit() } }
            .reversed()
            .toMutableList()
    }
}