package com.jonasbina.utils

import java.io.File

object InputUtils {
    fun getDayInputText(day: Int): Input {
        return Input(getDayInputFile(day).readText())
    }

    fun getDayInputLines(day: Int): List<String> {
        return getDayInputFile(day).readLines()
    }
    fun getTestInputText(day: Int): Input {
        return Input(getTestInputFile(day).readText())
    }
    private fun getDayInputFile(day: Int): File {
        return File("src/main/resources/day${day.toString().padStart(2, '0')}/input.txt")
    }
    private fun getTestInputFile(day: Int): File {
        return File("src/main/resources/day${day.toString().padStart(2, '0')}/test.txt")
    }
    fun getTestInputLines(day: Int): List<String> {
        return getTestInputFile(day).readLines()
    }

    private fun getDayInputFileName(day: Int): String {
        return buildString {
            append("day")
            append(day.toString().padStart(2, '0'))
            append(".txt")
        }
    }
    private fun getTestInputFileName(day: Int): String {
        return buildString {
            append("test")
            append(day.toString().padStart(2, '0'))
            append(".txt")
        }
    }
}
data class Input(
    val input:String,
){
    val inputLines = input.split('\n')
    val inputInts = inputLines.map { it.toIntOrNull() ?: 0 }
}
data class Inputs(
    val input:Input,
    val testInput: Input
)