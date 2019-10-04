package cz.jlinhart.barcamp


import java.awt.Color
import kotlin.math.abs

sealed class MaybeLine
object NoLine : MaybeLine()

class Line(val start: Point, val end: Point, val color: Color) : Drawable, MaybeLine() {

    private val xRange by lazy { start.x upTo end.x }
    private val yRange by lazy { (start.y upTo end.y) }
    private val isVertical by lazy { start.x == end.x }
    private val isHorizontal: Boolean by lazy { start.y == end.y }

    override fun draw(c: Canvas) {
        when {
            isVertical -> drawAsVertical(c)
            isHorizontal -> drawAsHorizontal(c)
            (abs(start.x - end.x) == abs(start.y - end.y)) -> drawAsDiagonal(c)
        }
    }

    private fun drawAsDiagonal(c: Canvas) {
        val x = xRange.iterator()
        yRange.forEach { y -> c.plot(x.next(), y) }
    }

    private fun drawAsHorizontal(c: Canvas) {
        val y = start.y
        xRange.forEach { x -> c.plot(x, y) }
    }

    private fun drawAsVertical(c: Canvas) {
        val x = start.x
        yRange.forEach { y -> c.plot(x, y) }
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
