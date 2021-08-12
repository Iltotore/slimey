package io.github.iltotore.frostburn.math

import indigo.{Point, Vector2}

case class AxisMovement(axis: Axis, length: Double) {
  
  lazy val toVector: Vector2 = axis match {
    case Axis.X => Vector2(length, 0)
    case Axis.Y => Vector2(0, length)
  }

  lazy val toPoint: Point = axis match {
    case Axis.X => Point(length.toInt, 0)
    case Axis.Y => Point(0, length.toInt)
  }
}
