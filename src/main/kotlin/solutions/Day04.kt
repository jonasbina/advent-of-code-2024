package com.jonasbina.solutions

import com.jonasbina.utils.*
import java.awt.Point

fun main() {
    val input = InputUtils.getDayInputText(4)
    val testInput = InputUtils.getTestInputText(4)
    val inputs = Inputs(input, testInput)
    Day04(inputs).run(correctResultPart1 = 18, correctResultPart2 = 9)
}

class Day04(
    override val inputs:Inputs
): Day(inputs){
    
    override fun part1(test:Boolean): Any {
        val input = if(test) inputs.testInput else inputs.input
        var total = 0
        input.inputLines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c->
                if (c=='X'){
                    val point = Point2D(x, y)
                    total+=Move.all().count {
                       point.step(it,input.inputLines)
                    }
                }
            }
        }
        return total
    }
    fun Point2D.step(move: Move, input:List<String>):Boolean{
        var dr = this
        val charsLeft = "MAS"
        for (ch in charsLeft){
            dr = dr.applyMove(move)
            if (dr.isInRange(input.size)){
                if (input[dr.y][dr.x] != ch){
                    return false
                }
            }else{
                return false
            }
        }
        return true
    }
    fun Point2D.getCharAt(lines:List<String>):Char=lines[y][x]

    override fun part2(test:Boolean): Any {
        val input = if(test) inputs.testInput else inputs.input
        val inputLines = input.inputLines
        var total = 0
        val size = inputLines.size
        inputLines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c->
                if (c in "SM"){
                    val oppositeCharacter = if (c=='S') 'M' else 'S'
                    val point = Point2D(x, y)
                    if (point.downRight().downRight().isInRange(size)){
                        if (point.downRight().getCharAt(inputLines)=='A'){
                            if (point.downRight().downRight().getCharAt(inputLines)==oppositeCharacter){
                                if (point.right().right().getCharAt(inputLines)==c){
                                    if (point.down().down().getCharAt(inputLines)==oppositeCharacter){
                                        total++
                                    }
                                }else{
                                    if (point.right().right().getCharAt(inputLines)==oppositeCharacter){
                                        if (point.down().down().getCharAt(inputLines)==c){
                                            total++
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return total
    }
}