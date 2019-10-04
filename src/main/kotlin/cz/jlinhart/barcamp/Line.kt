package cz.jlinhart.barcamp


import java.awt.Color
import kotlin.math.abs
import kotlin.math.max

sealed class MaybeLine
object NoLine : MaybeLine()

class Line(val start: Point, val end: Point, val color: Color) : Drawable, MaybeLine() {

    private val xRange by lazy { start.x upTo end.x }
    private val yRange by lazy { (start.y upTo end.y) }
    private val isVertical by lazy { start.x == end.x }
    private val isHorizontal by lazy { start.y == end.y }
    private val dy by lazy { abs(start.y - end.y).toDouble() }
    private val dx by lazy { abs(start.x - end.x).toDouble() }
    private val isDiagonal by lazy { dx == dy }
    private val dominantRangeSize: Int by lazy { max(dx, dy).toInt() + 1 }

    override fun draw(c: Canvas) {
        val (xProgression, yProgression) = when {
            isVertical -> verticalProgressions()
            isHorizontal -> horizontalProgressions()
            isDiagonal -> diagonalProgressions()
            else -> rasterProgressions()
        }
        xProgression.forEach { x -> c.plot(x, yProgression.next()) }
    }

    private data class RasterProgressions(val xProgression: Iterator<Int>, val yProgression: Iterator<Int>)

    private fun rasterProgressions(): RasterProgressions {
        return if (dx > dy) {
            RasterProgressions(xRange.iterator(), SlopedProgression(start.y, end.y, dy / dx))
        } else {
            RasterProgressions(SlopedProgression(start.x, end.x, dx / dy, dominantRangeSize), yRange.iterator())
        }
    }

    private fun verticalProgressions() =
        RasterProgressions(SingleValueProgression(start.x, dominantRangeSize), yRange.iterator())

    private fun horizontalProgressions() = RasterProgressions(xRange.iterator(), SingleValueProgression(start.y))
    private fun diagonalProgressions() = RasterProgressions(xRange.iterator(), yRange.iterator())

    inner class SlopedProgression(start: Int, end: Int, absSlope: Double, val limit: Int = Int.MAX_VALUE) :
        Iterator<Int> {
        var counter = 0
        private val slope = if (start < end) absSlope else -absSlope
        private var currentValue = start + 0.5
        override fun hasNext(): Boolean = counter++ < limit

        override fun next(): Int {
            return currentValue.toInt()
                .also { currentValue += slope }
        }

    }

    class SingleValueProgression(val value: Int, val limit: Int = Int.MAX_VALUE) : Iterator<Int> {
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
