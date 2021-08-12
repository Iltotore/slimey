package io.github.iltotore.frostburn

import indigo.shared.datatypes.Radians$package.Radians
import indigo.{IndigoLogger, Point, Rectangle, Vector2}

package object math {

  type Axis = Axis.Value

  lazy val MinVector2: Vector2 = Vector2(Double.MinValue)
  lazy val MaxVector2: Vector2 = Vector2(Double.MaxValue)

  extension (rectangle: Rectangle) {

    def collidingSides(vector: Vector2): Seq[Line2] = {
      Seq(
        vector.x match {
          case x if x > 0 => Some(Line2.fromPoints(rectangle.topLeft, rectangle.bottomLeft))
          case x if x == 0 => None
          case x if x < 0 => Some(Line2.fromPoints(rectangle.topRight, rectangle.bottomRight))
        },
        vector.y match {
          case y if y > 0 => Some(Line2.fromPoints(rectangle.topLeft, rectangle.topRight))
          case y if y == 0 => None
          case y if y < 0 => Some(Line2.fromPoints(rectangle.bottomLeft, rectangle.bottomRight))
        }
      ).flatten
    }
  }

  extension (vector: Vector2) {

    def rotate(angle: Radians): Vector2 = Vector2(
      Math.cos(angle.toDouble)*vector.x - Math.sin(angle.toDouble)*vector.y,
      Math.sin(angle.toDouble)*vector.x + Math.cos(angle.toDouble)*vector.y
    )

    def map(mapper: Double => Double): Vector2 = Vector2(mapper(vector.x), mapper(vector.y))
  }

  extension (point: Point) {

    def getAxisCoord(axis: Axis): Int = axis match {
      case Axis.X => point.x
      case Axis.Y => point.y
    }
  }
}
