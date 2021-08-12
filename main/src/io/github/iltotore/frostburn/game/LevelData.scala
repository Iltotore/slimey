package io.github.iltotore.frostburn.game

import indigo.{AssetName, Camera, Point, TiledGridMap, Zoom}

case class LevelData(mapData: GameMapData) {
  
  def createLevel: Level = Level(this, mapData.createGameMap, Point(32, 800), Player("Slimey", AssetName("player"), 15, Point(32, 800)), Camera(mapData.size.toPoint.copy(x = 0).moveBy(0, -40), Zoom.x1))
}