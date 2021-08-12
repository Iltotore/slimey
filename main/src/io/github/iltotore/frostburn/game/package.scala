package io.github.iltotore.frostburn

import indigo.shared.formats.TiledGridMap
import indigo.{TiledGridCell, TiledGridMap}

package object game {

  type TileRegistry = Map[Int, TileType]
  
  def TileRegistry(tileSet: Set[TileType]): TileRegistry = tileSet.map(tileType => (tileType.id, tileType)).toMap

  extension [A](grid: TiledGridMap[A]) {

    def mapTiles[B](mapper: A => B): TiledGridMap[B] = TiledGridMap(grid.layers.map(layer =>
      layer.copy(grid = layer.grid.map(cell => TiledGridCell(cell.column, cell.row, mapper(cell.tile))))
    ))
  }

  opaque type Weather = String

  object Weather {

    def apply(key: String): Weather = key

    val Sun = Weather("Sun")
    val Snow = Weather("Snow")
  }
}
