package cz.jlinhart.barcamp


import java.awt.Color

sealed class MaybeLine
object NoLine : MaybeLine()

class Line(val start: Point, val end: Point, val color: Color) : Drawable, MaybeLine() {

    private val xRange: IntProgression
        get() = start.x upTo end.x

    private infix fun Int.upTo(b: Int) = if (this < b) this..b else this downTo b

    override fun draw(c: Canvas) {
        val y = start.y
        xRange.forEach { x -> c.plot(x, y) }
    }

    private fun Canvas.plot(x: Int, y: Int) {
        try {
            pixel(Point(x, y), color)
        } catch (e: ArrayIndexOutOfBoundsException) {
            // no problem - just drawing outside of grid
        }
    }

}
