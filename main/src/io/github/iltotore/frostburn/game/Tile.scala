package io.github.iltotore.frostburn.game

import indigo.{IndigoLogger, Point, Radians, Rectangle, Vector2}
import io.github.iltotore.frostburn.math.*

trait Tile {

  def id: Int = tileType.id

  def tileType: TileType

  def collide(player: Player, movement: AxisMovement, tilePosition: Point): Option[Int] = None
}

object Tile {

  trait Collidable(val boundingBox: Rectangle) extends Tile {

    override def collide(player: Player, movement: AxisMovement, tilePosition: Point): Option[Int] = {
      val movedTileBox = boundingBox.moveTo(tilePosition)
      val movedPlayerBox = player.boundingBox.moveBy(movement.toPoint)
      if (movedTileBox.overlaps(movedPlayerBox)) movement match {
        case AxisMovement(Axis.X, length) if length > 0 => Some(movedTileBox.left - movedPlayerBox.width)
        case AxisMovement(Axis.X, length) if length < 0 => Some(movedTileBox.right)
        case AxisMovement(Axis.Y, length) if length > 0 => Some(movedTileBox.top - movedPlayerBox.height)
        case AxisMovement(Axis.Y, length) if length < 0 => Some(movedTileBox.bottom)
        case _ => super.collide(player, movement, tilePosition)
      } else super.collide(player, movement, tilePosition)
    }
  }

  case class Invariant(tileType: TileType) extends Tile
  case class Static(override val id: Int, tileType: TileType) extends Tile
}