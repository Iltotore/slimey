package io.github.iltotore.frostburn.math

import indigo.{Point, Vector2}

case class Line2(start: Point, vector: Vector2) {

  lazy val end: Point = start + vector.toPoint
  
  lazy val parts: (AxisMovement, AxisMovement) = (AxisMovement(Axis.X, vector.x), AxisMovement(Axis.Y, vector.y))

  def collide(other: Line2): Option[Point] = {

    val (x1, y1) = (start.x, start.y)
    val (x2, y2) = (end.x, end.y)
    val (x3, y3) = (other.start.x, other.start.y)
    val (x4, y4) = (other.end.x, other.end.y)

    val denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)
    val xNumerator = (x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)
    val yNumerator = (x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)

    Option.unless(denominator == 0)(Point(xNumerator/denominator, yNumerator/denominator))
  }
  
  def toSeq: Seq[Point] = Seq(start, end)
}

object Line2 {

  def fromVector(vector: Vector2): Line2 = Line2(Point.zero, vector)

  def fromPoints(start: Point, end: Point): Line2 = Line2(start, Vector2.fromPoints(start, end))
}
