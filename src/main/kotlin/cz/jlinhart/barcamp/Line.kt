package cz.jlinhart.barcamp


import java.awt.Color

sealed class MaybeLine
object NoLine : MaybeLine()

class Line(val start: Point, val end: Point, val color: Color) : Drawable, MaybeLine() {

    override fun draw(c: Canvas) {
    }

}