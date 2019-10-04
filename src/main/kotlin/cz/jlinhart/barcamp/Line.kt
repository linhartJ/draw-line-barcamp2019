package cz.jlinhart.barcamp


import java.awt.Color
import kotlin.math.abs

sealed class MaybeLine
object NoLine : MaybeLine()

class Line(val start: Point, val end: Point, val color: Color) : Drawable, MaybeLine() {

    private val xRange by lazy { (start.x upTo end.x).iterator() }
    private val yRange by lazy { (start.y upTo end.y).iterator() }
    private val isVertical by lazy { start.x == end.x }
    private val isHorizontal by lazy { start.y == end.y }
    private val dy by lazy { abs(start.y - end.y).toDouble() }
    private val dx by lazy { abs(start.x - end.x).toDouble() }
    private val isDiagonal by lazy { dx == dy }
    private val yRangeSize: Int by lazy { dy.toInt() + 1 }

    override fun draw(c: Canvas) {
        val (xProgress, yProgress) = when {
            isVertical -> verticalProgressions()
            isHorizontal -> horizontalProgressions()
            isDiagonal -> diagonalProgressions()
            else -> slopedProgressions()
        }
        xProgress.forEach { x -> c.plot(x, yProgress.next()) }
    }

    private data class LineProgressions(val xProgress: Iterator<Int>, val yProgress: Iterator<Int>)

    private fun slopedProgressions(): LineProgressions {
        val isMoreHorizontal = dx > dy
        return if (isMoreHorizontal) {
            LineProgressions(xRange, SlopedProgress(start.y, end.y, dy / dx))
        } else {
            LineProgressions(SlopedProgress(start.x, end.x, dx / dy, yRangeSize), yRange)
        }
    }

    private fun verticalProgressions() =
        LineProgressions(ConstantProgress(start.x, yRangeSize), yRange)

    private fun horizontalProgressions() = LineProgressions(xRange, ConstantProgress(start.y))
    private fun diagonalProgressions() = LineProgressions(xRange, yRange)

    class SlopedProgress(start: Int, end: Int, absSlope: Double, val limit: Int = Int.MAX_VALUE) : Iterator<Int> {
        var counter = 0
        private val slope = if (start < end) absSlope else -absSlope
        private var currentValue = start + 0.5
        override fun hasNext(): Boolean = counter++ < limit

        override fun next(): Int {
            return currentValue.toInt()
                .also { currentValue += slope }
        }

    }

    class ConstantProgress(val value: Int, val limit: Int = Int.MAX_VALUE) : Iterator<Int> {
        var counter = 0
        override fun hasNext(): Boolean = counter++ < limit
        override fun next(): Int = value
    }

    private fun Canvas.plot(x: Int, y: Int) {
        try {
            pixel(Point(x, y), color)
        } catch (e: ArrayIndexOutOfBoundsException) {
            // no problem - just drawing outside of grid
        }
    }

    private infix fun Int.upTo(b: Int) = if (this < b) this..b else this downTo b
}
