package com.jonasbina

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import kotlin.time.measureTime

fun main() {
    println(
        "What day to create?\nEnter day as an integer:"
    )
    val day = readln().toInt()
    val dayAsString = if (day > 9) day.toString() else "0$day"
    val codeFile = File("src/main/kotlin/solutions/Day$dayAsString.kt")
    if (!codeFile.exists()) {
        val fileContent = """package com.jonasbina.solutions

import com.jonasbina.utils.Day
import com.jonasbina.utils.InputUtils
import com.jonasbina.utils.Inputs

fun main() {
    val input = InputUtils.getDayInputText($day)
    val testInput = InputUtils.getTestInputText($day)
    val inputs = Inputs(input, testInput)
    Day$dayAsString(inputs).run(correctResultPart1 = 0, correctResultPart2 = 0)
}

class Day$dayAsString(
    override val inputs:Inputs
): Day(inputs){
    
    override fun part1(test:Boolean): Any {
        val input = if(test) inputs.testInput else inputs.input
        return 0
    }

    override fun part2(test:Boolean): Any {
        val input = if(test) inputs.testInput else inputs.input
        return 0
    }
}
        """.trimIndent()
        codeFile.writeText(fileContent)
    }
    val resFolder = File("src/main/resources/day$dayAsString")
    if (!resFolder.exists()) {
        resFolder.mkdir()
        File(resFolder.path + "/test.txt")
            .createNewFile()
        val inputFile = File(resFolder.path + "/input.txt")
        inputFile
            .createNewFile()
        inputFile.writeText(getInput(day))
    }
}

fun getInput(day: Int):String {
    val client = OkHttpClient()

    // Replace with your actual session cookie value
    val sessionCookie = File("session.txt").readText()

    val url = "https://adventofcode.com/2024/day/$day/input"
    val request = Request.Builder()
        .url(url)
        .addHeader("Cookie", "session=$sessionCookie")
        .build()
    var res = ""
    client.newCall(request).execute().use { response ->
        if (response.isSuccessful) {
            res = response.body?.string()?.trim() ?: ""
        } else {
            println("Failed to fetch input: ${response.code}")
        }
    }
    return res
}