package io.github.iltotore.frostburn.game

case class StartupData(levels: Seq[LevelData]) {
  
  def getLevelData(level: Int): Option[LevelData] = levels.lift(level)
}