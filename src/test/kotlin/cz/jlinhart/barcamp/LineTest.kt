package cz.jlinhart.barcamp

import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.awt.Color

@ExtendWith(MockitoExtension::class)
internal class LineTest {

    @Mock
    private lateinit var canvas: Canvas

    private val color = Color.WHITE

    @Test
    fun `drawing point outside of grid does not throw`() {
        // given
        val p = 4 to -1
        givenPointIsOutOfBounds(p)

        // when
        val action = { drawPoint(p) }

        // then
        assertDoesNotThrow(action)
    }

    @Test
    fun `line ending at its start draws single pixel`() {
        // given
        val p = 2 to 2

        // when
        drawPoint(p)

        // then
        assertPointDrawn(p)
    }

    @Test
    fun `horizontal line`() {
        // given
        val start = 2 to 3
        val end = 5 to 3

        // when
        drawLine(start, end)

        // then
        val expected = listOf(
            2 to 3,
            3 to 3,
            4 to 3,
            5 to 3
        )
        assertPointsDrawn(expected)
    }

    private fun assertPointsDrawn(expected: List<Point>) {
        val captor = argumentCaptor<Point>()
        verify(canvas, atLeast(0)).pixel(captor.capture(), any())
        val drawn = captor.allValues
        expected.forEachIndexed { idx, p -> assertEquals(drawn[idx], p) }
    }

    private fun assertPointDrawn(p: Point) {
        val captor = argumentCaptor<Point>()
        verify(canvas, atLeast(0)).pixel(captor.capture(), any())
        val drawn = captor.allValues.single()
        assertEquals(p, drawn)
    }

    private fun givenPointIsOutOfBounds(p: Point) {
        given(canvas.pixel(eq(p), any())).willThrow(ArrayIndexOutOfBoundsException())
    }

    private fun drawPoint(p: Point) {
        drawLine(p, p)
    }

    private fun drawLine(start: Point, end: Point) {
        Line(start, end, color).draw(canvas)
    }

    private infix fun Int.to(y: Int): Point = Point(this, y)
}