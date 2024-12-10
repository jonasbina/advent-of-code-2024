package com.jonasbina.solutions

import com.jonasbina.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext
import kotlin.math.truncate
import kotlin.system.measureTimeMillis

fun main() {
    val input = InputUtils.getDayInputText(9)
    val testInput = InputUtils.getTestInputText(9)
    val inputs = Inputs(input, testInput)
    Day09(inputs).run(correctResultPart1 = 1928, correctResultPart2 = 2858)
}

/**Let's not talk about day 9 okay...
 *
 */
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
        for (n in numberClusters){
            val d = dotClusters.firstOrNull { cluster ->
                cluster.first.size >= n.first.size && cluster.first.first().second < n.first.first().second
            }
            if (d != null){
                val dots = d.first
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
        println("calculating")
        var sum = 0L
        testPrintln(string.toString(), test)
        //this motherfucking mistake of not counting the dots in index which didnt cause error in part 1 made me really mad
        string.mapIndexed { index, s ->s to index  }.filter { it.first.first().isDigit() }.forEach{ c ->
            sum += c.first.toInt() * c.second
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