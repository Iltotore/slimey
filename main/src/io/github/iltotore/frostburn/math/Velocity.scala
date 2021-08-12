package io.github.iltotore.frostburn.math

import indigo.Vector2

trait Velocity {

  def toVector: Vector2

  def nextVelocity: Velocity
}

object Velocity {

  case class Base(vector: Vector2, inertness: Vector2, gravity: Vector2, min: Vector2, max: Vector2) extends Velocity {

    override def toVector: Vector2 = ((vector * inertness) + gravity)
      .max(min)
      .min(max)

    override def nextVelocity: Velocity = Base(toVector, inertness, gravity, min, max)
  }

  case class Mutable(var vector: Vector2, inertness: Vector2, gravity: Vector2, min: Vector2, max: Vector2) extends Velocity {

    override def toVector: Vector2 = ((vector * inertness) + gravity)
      .max(min)
      .min(max)

    override def nextVelocity: Velocity = {
      vector = toVector
      this
    }
  }

  case class Composed(children: Seq[Velocity], operator: (Vector2, Vector2) => Vector2) extends Velocity {

    override def toVector: Vector2 = children.map(_.toVector).reduce(operator)

    override def nextVelocity: Velocity = Composed(children.map(_.nextVelocity), operator)
  }

  def apply(
             vector: Vector2,
             inertness: Vector2 = Vector2.one,
             gravity: Vector2 = Vector2.zero,
             min: Vector2 = MinVector2,
             max: Vector2 = MaxVector2
           ): Velocity = Base(vector, inertness, gravity, min, max)

  def Mutable(
             vector: Vector2,
             inertness: Vector2 = Vector2.one,
             gravity: Vector2 = Vector2.zero,
             min: Vector2 = MinVector2,
             max: Vector2 = MaxVector2
           ): Velocity.Mutable = new Mutable(vector, inertness, gravity, min, max)

  lazy val Zero: Velocity = Velocity(Vector2.zero)

  given Numeric[Velocity] with {
    override def plus(x: Velocity, y: Velocity): Velocity = Composed(Seq(x, y), _ + _)

    override def minus(x: Velocity, y: Velocity): Velocity = Composed(Seq(x, y), _ - _)

    override def times(x: Velocity, y: Velocity): Velocity = Composed(Seq(x, y), _ * _)

    override def negate(x: Velocity): Velocity = minus(Velocity.Zero, x)

    override def fromInt(x: Int): Velocity = Velocity(Vector2(x), Vector2.zero, Vector2.one)

    override def parseString(str: String): Option[Velocity] = str.toDoubleOption.map(d => Velocity(Vector2(d)))

    override def toInt(x: Velocity): Int = x.toVector.length.toInt

    override def toLong(x: Velocity): Long = x.toVector.length.toLong

    override def toFloat(x: Velocity): Float = x.toVector.length.toFloat

    override def toDouble(x: Velocity): Double = x.toVector.length

    override def compare(x: Velocity, y: Velocity): Int = toDouble(x).compare(toDouble(y))
  }
}