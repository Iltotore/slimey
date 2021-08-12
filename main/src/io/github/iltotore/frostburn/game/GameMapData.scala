package io.github.iltotore.frostburn.game

import indigo.shared.formats.{TileSet, TiledGridMap}
import indigo.{Size, TiledGridMap}

case class GameMapData(grid: TiledGridMap[TileType], gridSize: Size, tileSize: Size, tileSheets: List[TileSheet]) {

  def createGameMap: GameMap = GameMap(
    grid.mapTiles(_.createTile(Weather.Sun)),
    gridSize,
    tileSize,
    new TiledMapRenderer(tileSheets)
  )

  val size = {
    println(s"sizes = $gridSize, $tileSize")
    gridSize * tileSize
  }
}