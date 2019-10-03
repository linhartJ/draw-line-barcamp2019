package cz.jlinhart.barcamp

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.util.*
import javax.swing.JPanel

class AwtCanvas(width: Int, height: Int) : Canvas, JPanel() {

    private val content: LinkedList<Drawable> = LinkedList()
    private val canvas: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

    init {
        setSize(width, height)
        preferredSize = size
        fillCanvas(Color.BLACK)
    }

    fun redrawCanvas() {
        resetCanvas()
        for (dr in content) {
            dr.draw(this)
        }
    }

    fun addDrawable(dr: Drawable) {
        content.addLast(dr)
    }

    fun removeLast() {
        if (!content.isEmpty()) {
            content.removeLast()
        }
    }

    private fun pixel(x: Int, y: Int, color: Color = Color.WHITE) {
        canvas.setRGB(x, y, color.rgb)
        repaint()
    }

    override fun pixel(p: Point, color: Color) {
        pixel(p.x, p.y, color)
    }

    private fun fillCanvas(color: Color) {
        val rgb = color.rgb
        for (x in 0 until canvas.width) {
            for (y in 0 until canvas.height) {
                canvas.setRGB(x, y, rgb)
            }
        }
        repaint()
    }

    fun resetCanvas() {
        fillCanvas(Color.BLACK)
    }

    fun clearContent() {
        content.removeAll(content)
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        val g2 = g as Graphics2D
        g2.drawImage(canvas, null, null)
    }

}
