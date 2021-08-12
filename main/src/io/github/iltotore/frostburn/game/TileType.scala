package io.github.iltotore.frostburn.game

import indigo.Rectangle

trait TileType {

  def id: Int

  def createTile(weather: Weather): Tile
}

object TileType {

  def Static(tileId: Int)(map: Map[Weather, TileType => Tile]): TileType = new TileType {

    override def id: Int = tileId

    override def createTile(weather: Weather): Tile = map(weather)(this)
  }

  def Invariant(tileId: Int)(tile: TileType => Tile): TileType = new TileType {
    override def id: Int = tileId

    override def createTile(weather: Weather): Tile = tile(this)
  }

  def Air(id: Int): TileType = Invariant(id)(Tile.Invariant.apply)

  def Unknown(id: Int): TileType = Invariant(id)(new Tile.Invariant(_) with Tile.Collidable(Rectangle(0, 0, 16, 16)))

  val Registry = TileRegistry(Set(
    Air(0),
    Static(210)(Map(
      Weather.Sun -> Tile.Invariant.apply,
      Weather.Snow -> (tileType => new Tile.Static(202, tileType) with Tile.Collidable(Rectangle(0, 0, 16, 16)))
    ))
  ))
}