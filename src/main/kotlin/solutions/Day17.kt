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
    Day17(inputs).run(correctResultPart1 = "5,7,3,0", correctResultPart2 = 117440L)
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
        registerA = (registerA / 2.0.pow(operand.toDouble())).toLong()
        ip += 2
    }

    fun bxl(operand: Long) {
        registerB = registerB.xor(operand).toLong()
        ip += 2
    }

    fun bst(operand: Long) {
        registerB = (operand % 8).toLong()
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
        registerB = registerB.xor(registerC)
        ip += 2
    }

    fun out(operand: Long) {
        printHistory.add((operand % 8).toString())
        ip += 2
    }

    fun bdv(operand: Long) {
        registerB = (registerA / 2.0.pow(operand.toDouble())).toLong()
        ip += 2
    }

    fun cdv(operand: Long) {
        registerC = (registerA / 2.0.pow(operand.toDouble())).toLong()
        ip += 2
    }

    data class Action(
        val opcode: Long,
        val operand: Long
    )

    override fun part2(test: Boolean): Any {
        var correctAs = mutableSetOf<String>()
        for (i in 1..actions.size){
            val currentActions = actions.takeLast(i)
            val newCorrectAs = correctAs.toMutableList()
            for (correctA in correctAs) {
                val list = (0L..7L).toList().mapNotNull { a ->
                    val value = correctA.toLong()*8L+a
                    val simulated = simulateForA(value)
                    if(simulated == currentActions) value else null
                }
                if (list.isEmpty()) {
                    newCorrectAs.removeAt(newCorrectAs.indexOf(correctA))
                } else {
                    newCorrectAs[newCorrectAs.indexOf(correctA)] = list[0].toString()
                    list.drop(1).forEach {
                        newCorrectAs.add(it.toString())
                    }
                }
            }
            if (i==1) {
                for (a in 0L..7L) {
                    if (simulateForA(a) == currentActions) {
                        newCorrectAs.add(a.toString())
                    }
                }
            }
            correctAs = newCorrectAs.toMutableSet()
        }
        return correctAs.minByOrNull {
            it.length
        }?.toLong()?:0L
    }

    /**
     *Would take like 50ys for the full input :)
     */
    fun bruteforcePart2(): Long {
        var a = 1L
        while (a < Long.MAX_VALUE) {
            if (simulateForA(a) == actions) {
                break
            }
            a++
        }
        return a
    }

    fun simulateForA(a: Long): List<Int> {
        ip = 0
        printHistory.clear()
        registerA = a
        registerB = 0
        registerC = 0
        while (ip < actions.lastIndex/*intentionally < not <= so i can get the next element too :)*/) {
            Action(actions[ip].toLong(), actions[ip + 1].toLong()).apply()
        }
        return printHistory.map { it.toInt() }
    }

    fun simulateOutput(a: Long): Long {
        registerA = a
        registerB = 0
        registerC = 0
        registerB = registerA % 8
        registerB = registerB.xor(3)
        registerC = (registerA / (2.0.pow(registerB.toDouble()))).toLong()
        registerB = registerB.xor(registerC)
        //i can divide the A here by 8 but its quite useless to do here
        registerB = registerB.xor(5)
        return registerB % 8
    }
}