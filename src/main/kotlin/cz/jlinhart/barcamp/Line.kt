package cz.jlinhart.barcamp


import java.awt.Color

sealed class MaybeLine
object NoLine : MaybeLine()

class Line(val start: Point, val end: Point, val color: Color) : Drawable, MaybeLine() {

    override fun draw(c: Canvas) {
        val y = start.y
        (start.x..end.x).forEach { x -> c.plot(Point(x, y)) }
    }

    private fun Canvas.plot(point: Point) {
        try {
            pixel(point, color)
        } catch (e: ArrayIndexOutOfBoundsException) {
            // no problem - just drawing outside of grid
        }
    }

}