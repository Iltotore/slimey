package io.github.iltotore.frostburn.game

import indigo.*
import indigo.shared.formats.TileSet

class TiledMapRenderer(tileSheets: List[TileSheet]) {

  private def fromIndex(index: Int, gridWidth: Int): Point = Point(
    x = index % gridWidth,
    y = index / gridWidth
  )

  def toGroup(map: GameMap): Group = Group(
    for {
      sheet <- tileSheets.headOption.toList
      tileSheetColumnCount <- sheet.set.columns.toList
      layers <- createLayers(map, sheet, tileSheetColumnCount)
    } yield Group(layers)
  )

  def createLayers(gameMap: GameMap, sheet: TileSheet, tileSheetColumnCount: Int): List[Group] = {
    val tileSize: Size = gameMap.tileSize
    gameMap.grid.layers.map { layer =>
      val tilesInUse: Map[Int, Graphic[Material.Bitmap]] =
        layer.grid.map(cell =>
          (
            cell.tile.id,
            Graphic(Rectangle(Point.zero, tileSize), 1, Material.Bitmap(sheet.assetName))
              .withCrop(
                Rectangle(fromIndex(cell.tile.id - 1, tileSheetColumnCount) * tileSize.toPoint, tileSize)
              )
          )
        ).toMap

      Group(
        layer.grid.flatMap { cell =>
          if (cell.tile.id == 0) Nil
          else {
            tilesInUse
              .get(cell.tile.id)
              .map(g => List(g.moveTo(Point(cell.column, cell.row) * tileSize.toPoint)))
              .getOrElse(Nil)
          }
        }
      )
    }.toList
  }
}
