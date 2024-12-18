package com.jonasbina.solutions

import com.jonasbina.utils.Day
import com.jonasbina.utils.InputUtils
import com.jonasbina.utils.Inputs
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow
import kotlin.random.Random

fun main() {
    val input = InputUtils.getDayInputText(17)
    val testInput = InputUtils.getTestInputText(17)
    val inputs = Inputs(input, testInput)
    Day17(inputs).run(correctResultPart1 = "5,7,3,0", correctResultPart2 = 117440)
}

class Day17(
    override val inputs: Inputs
) : Day(inputs) {
    var registerA = 0L
    var registerB = 0L
    var registerC = 0L
    var ip = 0
    var actions: List<Int> = listOf()
    val printHistory = mutableListOf<String>()
    override fun part1(test: Boolean): Any {
        ip = 0
        printHistory.clear()
        val inputLines = if (test) inputs.testInput.inputLines else inputs.input.inputLines
        registerA = inputLines[0].split(": ")[1].toLong()
        registerB = inputLines[1].split(": ")[1].toLong()
        registerC = inputLines[2].split(": ")[1].toLong()
        actions = inputLines[4].split(": ")[1].split(",").map { it.toInt() }
        while (ip < actions.lastIndex/*intentionally < not <= so i can get the next element too :)*/) {
            Action(actions[ip].toLong(), actions[ip + 1].toLong()).apply()
        }
        return printHistory.joinToString(",")
    }

    fun Action.apply() {

        val comboOperand = when (this.operand) {
            4L -> registerA
            5L -> registerB
            6L -> registerC
            else -> this.operand
        }
        when (this.opcode) {
            0L -> adv(comboOperand);
            1L -> bxl(operand);
            2L -> bst(comboOperand)
            3L -> jnz(operand)
            4L -> bxc(comboOperand)
            5L -> out(comboOperand)
            6L -> bdv(comboOperand)
            7L -> cdv(comboOperand)
        }
    }

    fun adv(operand: Long) {
        registerA = (registerA / 2.0.pow(operand.toDouble())).toInt().toLong()
        ip += 2
    }

    fun bxl(operand: Long) {
        registerB = registerB.xor(operand).toInt().toLong()
        ip += 2
    }

    fun bst(operand: Long) {
        registerB = (operand % 8).toInt().toLong()
        ip += 2
    }

    fun jnz(operand: Long) {
        if (registerA != 0L) {
            ip = operand.toInt()
        } else {
            ip += 2
        }
    }

    fun bxc(operand: Long) {
        registerB = registerB.xor(registerC).toInt().toLong()
        ip += 2
    }

    fun out(operand: Long) {
        printHistory.add((operand % 8).toString())
        ip += 2
    }

    fun bdv(operand: Long) {
        registerB = (registerA / 2.0.pow(operand.toDouble())).toInt().toLong()
        ip += 2
    }

    fun cdv(operand: Long) {
        registerC = (registerA / 2.0.pow(operand.toDouble())).toInt().toLong()
        ip += 2
    }

    data class Action(
        val opcode: Long,
        val operand: Long
    )

    override fun part2(test: Boolean): Any {
        if (test) return 0L
        val min = 0L
        val max = 999999999999999
        var solution = 0L
        var population = Array(10000){ Random((System.currentTimeMillis()*it).toInt()).nextLong(min,max)}
        while (solution==0L){
            val sorted = population.map {
                it to simulateForA(it)
            }.map {
                it to calculateSimilarity(it.second, actions)
            }.sortedByDescending { it.second }
            println(sorted.first().second)
            val correct = sorted.firstOrNull { it.first.second==actions}
            if (correct!=null){
                solution = correct.first.first
            }
            val best = sorted.map {x->
                Array(x.second){x.first}.toList()
            }.flatten().map { it.first }.shuffled().take(sorted.size/100)
            val new = best.toMutableSet()
            while (new.size!=10000){
                val random = best.shuffled().first()
                val mutation = if (Random.nextDouble() < 0.5) Random.nextLong(-100, 100) else Random.nextLong(-100000, 100000)
                new.add(random + mutation)
            }
            population=new.toTypedArray()
        }
        return solution
    }
    fun calculateSimilarity(l1:List<Int>,l2:List<Int>): Int {
        println(l1.size to l2.size)
        if (l1.size!=l2.size) return maxOf(l1.size,l2.size)-abs(l2.size-l1.size)
        val difference = l1.mapIndexed { index, i ->
            abs(i-l2[i])
        }.sum()
        println(difference)
        return l1.size*10-difference
    }

    fun simulateForA(a:Long):List<Int>{
        ip = 0
        printHistory.clear()
        registerA = a
        registerB = 0
        registerC = 0
        while (ip < actions.lastIndex/*intentionally < not <= so i can get the next element too :)*/) {
            Action(actions[ip].toLong(), actions[ip + 1].toLong()).apply()
        }
        println(printHistory)
        return printHistory.map { it.toInt() }
    }
}