package cz.jlinhart.barcamp


import java.awt.Color

sealed class MaybeLine
object NoLine : MaybeLine()

class Line(val start: Point, val end: Point, val color: Color) : Drawable, MaybeLine() {

    private val xRange by lazy { start.x upTo end.x }
    private val yRange by lazy { (start.y upTo end.y) }
    private val isVertical by lazy { start.x == end.x }

    override fun draw(c: Canvas) {
        if (isVertical) {
            drawAsVertical(c)
        } else {
            drawAsHorizontal(c)
        }
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
