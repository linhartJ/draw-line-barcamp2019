package cz.jlinhart.barcamp

object PointComparator : Comparator<Point> {
    override fun compare(o1: Point, o2: Point): Int {
        return when {
            o1.x < o2.x -> 1
            o1.x > o2.x -> -1
            o1.y < o2.y -> 1
            o1.y > o2.y -> -1
            else -> 0
        }
    }

}