package cz.jlinhart.barcamp

import com.nhaarman.mockitokotlin2.*
import cz.jlinhart.barcamp.LineTest.LineType.*
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

    enum class LineType(
        val start: Point,
        val end: Point,
        val expected: List<Point>
    ) {
        SINGLE_PIXEL(2 to 2, 2 to 2, listOf(2 to 2)),
        HORIZONTAL_LINE(2 to 3, 5 to 3, EXPECTED_HORIZONTAL_LINE),
        HORIZONTAL_LINE_REVERSED(5 to 3, 2 to 3, EXPECTED_HORIZONTAL_LINE),
        VERTICAL_LINE(1 to 1, 1 to 3, EXPECTED_VERTICAL_LINE),
        VERTICAL_LINE_REVERSED(1 to 3, 1 to 1, EXPECTED_VERTICAL_LINE),
    }

    private fun LineType.test() {
        // given
        reset(canvas)

        // when
        drawLine(start, end)

        // then
        assertPointsDrawn(expected)
    }

    @Test
    fun `all line types renders correctly`() {
        SINGLE_PIXEL.test()
        HORIZONTAL_LINE.test()
        HORIZONTAL_LINE_REVERSED.test()
        VERTICAL_LINE.test()
        VERTICAL_LINE_REVERSED.test()
    }

    private fun assertPointsDrawn(expected: List<Point>) {
        val captor = argumentCaptor<Point>()
        verify(canvas, atLeast(0)).pixel(captor.capture(), any())
        val drawn = captor.allValues.sortedWith(PointComparator).toSimpleString()
        val expectedString = expected.sortedWith(PointComparator).toSimpleString()
        assertEquals(expectedString, drawn)
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

    private fun Collection<Point>.toSimpleString(): String {
        return joinToString { p -> "[${p.x}, ${p.y}]" }
    }

}

private infix fun Int.to(y: Int): Point = Point(this, y)

private val EXPECTED_HORIZONTAL_LINE = listOf(
    2 to 3,
    3 to 3,
    4 to 3,
    5 to 3
)


private val EXPECTED_VERTICAL_LINE = listOf(
    1 to 1,
    1 to 2,
    1 to 3
)
