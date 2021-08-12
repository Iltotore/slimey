package io.github.iltotore.frostburn.game

import indigo.{Group, IndigoLogger, Key, Point, Rectangle, Size}

case class GameModel(screenSize: Size, hold: Map[Key, GameModel => Unit] = Map.empty, currentLevel: Option[Level] = None) {

  val screenDimensions: Rectangle = Rectangle(Point(0, 0), screenSize)

  val screenCenter: Point = screenDimensions.center

  def play(data: LevelData): GameModel = copy(currentLevel = Some(data.createLevel))

  def toGroup: Group = currentLevel.map(_.toGroup).getOrElse(Group.empty)

  def press(key: Key)(action: GameModel => Unit): GameModel =
    if (hold.contains(key)) this
    else {
      println(s"holding $key")
      copy(hold = hold.updated(key, action))
    }

  def release(key: Key): GameModel = copy(hold = hold.removed(key))

  def tick(): Unit = {
    hold.values.foreach(_.apply(this))
    currentLevel.map(_.tick(this))
  }
}