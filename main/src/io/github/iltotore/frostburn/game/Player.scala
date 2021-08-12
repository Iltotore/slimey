package io.github.iltotore.frostburn.game

import indigo.*
import io.github.iltotore.frostburn.math.*
import io.github.iltotore.frostburn.math.Velocity.given_Numeric_Velocity.mkNumericOps
import scala.math.Numeric.Implicits.*

class Player(val name: String, val assetName: AssetName, val maxSpeed: Double, var position: Point) {

  private var grounded: Boolean = false

  val gravity: Velocity.Mutable = Velocity.Mutable(
    vector = Vector2.zero,
    gravity = Vector2(0, 1),
    max = MaxVector2.withY(10)
  )

  val movement: Velocity.Mutable = Velocity.Mutable(
    vector = Vector2.zero,
    inertness = Vector2(0.75, 1),
    max = MaxVector2.withX(maxSpeed),
    min = MinVector2.withY(-maxSpeed)
  )

  var velocity: Velocity = gravity + movement

  val size: Size = Size(32, 32)

  def boundingBox: Rectangle = Rectangle(position, size)

  def tick(model: GameModel, level: Level): Unit = {
    velocity = velocity.nextVelocity
    val move = Line2(position, velocity.toVector)
    val collision = level.map.collide2(this, move, Depth(0))

    collision match {

      case (Some(x), Some(y)) =>
        grounded = true
        gravity.vector = Vector2(0, 0)
        position = Point(x, y)

      case (Some(x), None) =>
        grounded = false
        gravity.vector = gravity.vector.withX(0)
        position = Point(x, move.end.y)

      case (None, Some(y)) =>
        grounded = true
        gravity.vector = gravity.vector.withY(0)
        position = Point(move.end.x, y)

      case _ =>
        grounded = false
        position = move.end
    }
  }

  def moveX(x: Double): Unit = {
    println(s"move x = $x")
    movement.vector = movement.vector + Vector2(x, 0)
  }

  def jump(): Unit = if (grounded) {
    gravity.vector = gravity.vector.withY(-20)
    println("Jumping")
  }

  def present: SceneNode = Graphic(boundingBox, 1, Material.Bitmap(AssetName("slimey")))
    .withCrop(0, 0, 16, 16)
    .withScale(Vector2(2, 2))
}