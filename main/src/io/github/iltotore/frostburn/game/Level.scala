package io.github.iltotore.frostburn.game

import indigo.{Camera, Group, IndigoLogger, Point, TiledGridMap}

case class Level(data: LevelData, map: GameMap, spawn: Point, player: Player, var camera: Camera) {

  private var mapGroupDirty: Boolean = true
  private var currentWeather: Weather = Weather.Sun

  def weather: Weather = currentWeather

  def weather_=(value: Weather): Unit = {
    println(s"Changing weather to $value")
    map.mapTile(_.tileType.createTile(weather))
    this.mapGroupDirty = true
    this.currentWeather = value
  }
  
  def changeWeather(): Unit = weather = if(currentWeather == Weather.Sun) Weather.Snow else Weather.Sun

  private var mapGroupCache: Group = map.toGroup

  private def mapGroup: Group = {
    if (mapGroupDirty) {
      mapGroupDirty = false
      mapGroupCache = map.toGroup
    }
    mapGroupCache
  }

  private def absoluteGroup: Group = Group(mapGroup, player.present)

  def toGroup: Group = absoluteGroup.moveTo(camera.position * Point(-1, -1))

  def tick(model: GameModel): Unit = {
    player.tick(model, this)
    if (player.position.y > map.size.height+model.screenSize.height) player.position = spawn
    camera = camera.copy(position = player.boundingBox.center - (model.screenDimensions.center / 2))
  }
}