package io.github.iltotore.frostburn.game

import indigo.AssetName
import indigo.shared.formats.TileSet

case class TileSheet(set: TileSet, assetName: AssetName)

object TileSheet {

  def apply(set: TileSet): TileSheet = TileSheet(set, AssetName(s"tileset_${set.name.getOrElse("unknown")}"))
}