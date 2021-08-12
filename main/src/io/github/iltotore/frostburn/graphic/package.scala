package io.github.iltotore.frostburn

import indigo.*

package object graphic {

  def fillScale(originalSize: Vector2, screenSize: Vector2): Vector2 =
    Vector2(Math.max(screenSize.x / originalSize.x, screenSize.y / originalSize.y))

  extension [M <: Material](graphic: Graphic[M]) {

    def fill(viewportSize: Size, center: Point): Graphic[M] = graphic
      .withRef((graphic.size/2).toPoint)
      .moveTo(center)
      .scaleBy(fillScale(graphic.size.toVector, viewportSize.toVector))
  }
}
