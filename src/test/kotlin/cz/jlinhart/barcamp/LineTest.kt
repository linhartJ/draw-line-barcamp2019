package cz.jlinhart.barcamp

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.awt.Color

internal class LineTest {

    @Test
    fun `drawing point outside of grid does not throw`() {
        // given
        val p = Point(4, -1)
        val line = Line(p, p, Color.WHITE)
        val canvas = mock<Canvas> { on { pixel(eq(p), any()) } doThrow ArrayIndexOutOfBoundsException() }

        // when
        val action = { line.draw(canvas) }

        // then
        assertDoesNotThrow(action)
    }
}