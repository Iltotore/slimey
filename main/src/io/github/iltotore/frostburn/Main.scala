package io.github.iltotore.frostburn

import indigo.*
import indigo.shared.events.KeyboardEvent.{KeyDown, KeyUp}
import json.Json
import io.github.iltotore.frostburn.game.{GameMapData, GameModel, LevelData, StartupData, Tile, TileSheet, TileType}
import io.github.iltotore.frostburn.graphic.*

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("IndigoGame")
object Main extends IndigoSandbox[StartupData, GameModel] {

  val config: GameConfig = GameConfig.default

  val animations: Set[Animation] = Set()

  val assets: Set[AssetType] = Set(
    AssetType.Image(GameAsset.DayBackground.assetName, AssetPath("assets/day_background.png")),
    AssetType.Image(GameAsset.SunWeather, AssetPath("assets/map/sun.png")),
    AssetType.Image(GameAsset.SnowWeather, AssetPath("assets/map/snow.png")),
    AssetType.Image(GameAsset.Slimey, AssetPath("assets/slimey.png"))
  ) ++ indexedLevels

  private def indexedLevels: Seq[AssetType] = for (x <- 1 to 1) yield
    AssetType.Text(AssetName(s"level_$x"), AssetPath(s"assets/map/level/$x.json"))

  val fonts: Set[FontInfo] = Set()

  val shaders: Set[Shader] = Set()

  def setup(assetCollection: AssetCollection, dice: Dice): Outcome[Startup[StartupData]] = {
    val levels = for {
      levelAsset <- assetCollection.texts
      if levelAsset.name.toString startsWith "level_"
      rawMap <- Json.tiledMapFromJson(levelAsset.data)
      gridMap <- rawMap.toGrid(id => TileType.Registry.getOrElse(id, TileType.Unknown(id)))
    } yield LevelData(GameMapData(
      gridMap,
      Size(rawMap.width, rawMap.height),
      Size(rawMap.tilewidth, rawMap.tileheight),
      rawMap.tilesets.map(TileSheet.apply)
    ))

    Outcome(Startup.Success(StartupData(levels)))
  }

  def initialModel(startupData: StartupData): Outcome[GameModel] = {
    Outcome(GameModel(config.viewport.size))
  }

  def updateModel(context: FrameContext[StartupData], model: GameModel): GlobalEvent => Outcome[GameModel] = {

    case ViewportResize(viewport) => Outcome(model.copy(screenSize = viewport.size))

    case FrameTick =>
      model.tick()
      Outcome(model)

    case KeyDown(Key.ENTER) => Outcome(model.play(context.startUpData.getLevelData(0).get))

    case KeyDown(GameInput.DOWN_ARROW) =>
      model.currentLevel.foreach(_.changeWeather())
      Outcome(model)

    case KeyDown(key) =>
      Outcome(
        GameInput.Hold
          .get(key)
          .map(model.press(key)(_))
          .getOrElse(model)
      )

    case KeyUp(key) => Outcome(model.release(key))

    case _ => Outcome(model)
  }

  def present(context: FrameContext[StartupData], model: GameModel): Outcome[SceneUpdateFragment] = {
    Outcome(
      SceneUpdateFragment(List(
        Graphic(GameAsset.DayBackground.dimensions, 1, Material.Bitmap(GameAsset.DayBackground.assetName))
          .fill(model.screenSize, model.screenCenter)
      ) ++ model.currentLevel.map(_.toGroup))
    )
  }

}