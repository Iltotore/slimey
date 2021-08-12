package io.github.iltotore.frostburn

import indigo.{AssetName, Point, Rectangle, Size}

object GameAsset {

  trait Asset(name: String) {

    val assetName = AssetName(name)
  }

  case class ImageAsset(name: String, size: Size) extends Asset(name) {

    val dimensions: Rectangle = Rectangle(Point(0, 0), size)

    val center: Point = dimensions.center
  }

  val DayBackground = ImageAsset("day_background", Size(1024, 768))

  val SunWeather = AssetName("tileset_sun")
  val SnowWeather = AssetName("tileset_snow")
  
  val Slimey = AssetName("slimey")
}
