package io.github.iltotore.frostburn.game

import indigo.{Depth, Group, Point, Size, TiledGridMap, Vector2}
import io.github.iltotore.frostburn.math.*

case class GameMap(var grid: TiledGridMap[Tile], gridSize: Size, tileSize: Size, renderer: TiledMapRenderer) {
  
  lazy val size: Size = gridSize * tileSize
  
  def mapTile(mapper: Tile => Tile): Unit = this.grid = grid.mapTiles(mapper)

  def collide(player: Player, movement: AxisMovement, depth: Depth): Option[Int] = {
    var result: Option[Int] = None
    val axisCoord = player.position.getAxisCoord(movement.axis)
    for {
      layer <- grid.layers.toList.lift(depth.toInt).toSeq
      cell <- layer.grid
      coord <- cell.tile.collide(player, movement, Point(cell.column, cell.row)*tileSize.toPoint)
    } coord match {
      case x if result.forall(old => Math.abs(axisCoord - x) < Math.abs(axisCoord - old)) => result = Some(x)
      case _ =>
    }
    result
  }

  def collide2(player: Player, movement: Line2, depth: Depth): (Option[Int], Option[Int]) = (
    collide(player, movement.parts._1, depth),
    collide(player, movement.parts._2, depth)
  )

  def toGroup: Group = renderer.toGroup(this)
}
