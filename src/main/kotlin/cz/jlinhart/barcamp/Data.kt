package cz.jlinhart.barcamp

import java.awt.Color

interface Drawable {
    fun draw(c: Canvas)
}

interface Canvas {
    @Throws(ArrayIndexOutOfBoundsException::class)
    fun pixel(p: Point, color: Color = Color.WHITE)
}

data class Point(
    val x: Int,
    val y: Int
)

class LineInProgress {
    var start: Point? = null
    var end: Point? = null

    fun start(x: Int, y: Int) {
        start = Point(x, y)
    }

    fun end(x: Int, y: Int) {
        end = Point(x, y)
    }

    fun isInProgress(): Boolean = start != null

    fun finish(color: Color): MaybeLine {
        val s = start ?: return NoLine
        val e = end ?: return NoLine
        return Line(s, e, color)
    }

    fun reset() {
        start = null
        end = null
    }
}

