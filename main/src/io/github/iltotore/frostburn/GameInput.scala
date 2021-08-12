package io.github.iltotore.frostburn

import indigo.shared.constants.Key
import indigo.{IndigoLogger, Key}
import io.github.iltotore.frostburn.game.{GameModel, Player}
import io.github.iltotore.frostburn.math.{Axis, AxisMovement}

object GameInput {

  val LEFT_ARROW: Key = Key(37, "ArrowLeft")
  val UP_ARROW: Key = Key(38, "ArrowUp")
  val RIGHT_ARROW: Key = Key(39, "ArrowRight")
  val DOWN_ARROW: Key = Key(40, "ArrowDown")

  val F3: Key = Key(114, "F3")

  val Hold: Map[Key, GameModel => Unit] = Map(
    RIGHT_ARROW -> ((player: Player) => player.moveX(4)),
    LEFT_ARROW -> ((player: Player) => player.moveX(-4)),
    UP_ARROW -> ((player: Player) => player.jump()),
    Key.SPACE -> ((player: Player) => player.jump()),
    F3 -> ((player: Player) => IndigoLogger.debugOnce(s"playerPos=${player.position}"))
  ).map((key, value) => ((key, (model: GameModel) => model.currentLevel.map(_.player).foreach(value))))
}
