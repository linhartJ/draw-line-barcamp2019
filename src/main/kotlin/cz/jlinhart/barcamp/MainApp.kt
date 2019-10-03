package cz.jlinhart.barcamp

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Image
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import javax.swing.*

class MainApp : JFrame("Barcamp HK 2019") {

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        isResizable = false
        contentPane.add(LineDrawPanel())

        pack()
    }
}

class LineDrawPanel : JPanel() {
    private val lineProgress = LineInProgress()
    private val canvas: AwtCanvas = AwtCanvas(800, 500)
        .apply { addMouseListener(CanvasMouseListener()) }
        .apply { addMouseMotionListener(CanvasMouseMotionListener()) }

    init {
        val undoBtn = btn("Undo") { undo() }
        val eraserBtn = btn("Eraser") { clear() }
        val toolbar = toolbar(undoBtn, eraserBtn)
        layout = BorderLayout()
        add(toolbar, BorderLayout.WEST)
        add(canvas, BorderLayout.CENTER)
    }

    inner class CanvasMouseListener : MouseAdapter() {

        override fun mousePressed(e: MouseEvent) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                lineProgress.start(e.x, e.y)
            }
        }

        override fun mouseDragged(e: MouseEvent) {
            if (lineProgress.isInProgress()) {
                canvas.draw(lineProgress.finish(Color.BLACK))
                lineProgress.end(e.x, e.y)
                canvas.draw(lineProgress.finish(Color.WHITE))
            }
        }

        override fun mouseReleased(e: MouseEvent) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                lineProgress.end(e.x, e.y)
                canvas.addDrawable(lineProgress.finish(Color.WHITE) as Line)
                canvas.redrawCanvas()
                lineProgress.reset()
            }
        }
    }

    inner class CanvasMouseMotionListener : MouseMotionAdapter() {
        override fun mouseDragged(e: MouseEvent) {
            if (lineProgress.isInProgress()) {
                canvas.draw(lineProgress.finish(Color.BLACK))
                lineProgress.end(e.x, e.y)
                canvas.draw(lineProgress.finish(Color.WHITE))
            }
        }
    }

    private fun clear() {
        canvas.clearContent()
        canvas.resetCanvas()
        lineProgress.reset()
    }

    private fun undo() {
        canvas.removeLast()
        canvas.redrawCanvas()
        lineProgress.reset()
    }

    private fun toolbar(vararg btns: JButton): JToolBar {
        return JToolBar()
            .apply {
                orientation = JToolBar.VERTICAL
                isFloatable = false
                btns.forEach { add(it) }
                isVisible = true
            }
    }

    private fun Canvas.draw(something: Any) {
        if (something is Drawable) {
            something.draw(this)
        }
    }

    private fun btn(name: String, onClick: (ActionEvent) -> Unit = {}): JButton {
        return JButton()
            .apply {
                icon = ImageIcon(img(name.toLowerCase()))
                toolTipText = name
                addActionListener(onClick)
            }
    }

    private fun img(name: String): Image {
        val url = javaClass.getResource("/img/$name.png")
        return Toolkit.getDefaultToolkit().getImage(url)
    }
}


fun main() {
    SwingUtilities.invokeLater { MainApp() }
}