package com.jonasbina.utils

import kotlinx.serialization.Serializable
import java.lang.Math.toDegrees
import kotlin.math.abs
import kotlin.math.atan2
@Serializable
data class Point2DLong(val x: Long, val y: Long) {

    fun up() = applyMove(Move.up)
    fun down() = applyMove(Move.down)
    fun left() = applyMove(Move.left)
    fun right() = applyMove(Move.right)

    fun upLeft() = applyMove(Move.upLeft)
    fun upRight() = applyMove(Move.upRight)
    fun downLeft() = applyMove(Move.downLeft)
    fun downRight() = applyMove(Move.downRight)

    fun isLegal(maxX:Int,maxY:Int,minX:Int=0, minY:Int=0) = x>=minX && y>=minY && x<=maxX && y<=maxY

    fun distanceTo(other: Point2D): Long =
        abs(x - other.x) + abs(y - other.y)

    // Note: Tested only with x,y in screen mode (upper left)
    fun angleTo(other: Point2D): Double {
        val d = toDegrees(
            atan2(
                (other.y - y).toDouble(),
                (other.x - x).toDouble()
            )
        ) + 90
        return if (d < 0) d + 360 else d
    }
    fun getInput(
        input: List<String>
    ):Char?{
        return if (isInRange(input.size)){
            input[y.toInt()][x.toInt()]
        }else{
            null
        }
    }
    fun isInRange(xsize:Int,ysize:Int=xsize):Boolean = x<xsize&&y<ysize&&x>=0&&y>=0
    fun applyMove(move: Move) = copy(x = x + move.dx, y = y + move.dy)
    fun applyMoveLong(move: MoveLong) = copy(x = x + move.dx, y = y + move.dy)
}
data class MoveLong(val dx: Long, val dy: Long)