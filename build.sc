import mill._
import mill.scalalib._
import mill.scalajslib._
import mill.scalajslib.api._

import $ivy.`io.indigoengine::mill-indigo:0.9.0`, millindigo._

object main extends ScalaJSModule with MillIndigo {
  def scalaVersion = "3.0.0"

  def scalaJSVersion = "1.6.0"

  val gameAssetsDirectory: os.Path = os.pwd / "assets"
  val showCursor: Boolean = true
  val title: String = "Slimey's Adventure"
  val windowStartWidth: Int = 720 // Width of Electron window, used with `indigoRun`.
  val windowStartHeight: Int = 480 // Height of Electron window, used with `indigoRun`.

  def ivyDeps = Agg(
    ivy"io.indigoengine::indigo-json-circe::0.9.0",
    ivy"io.indigoengine::indigo::0.9.0"
  )

  def buildGame() = T.command {
    T {
      compile()
      fastOpt()
      indigoBuild()() // Note the double parenthesis!
    }
  }

  def runGame() = T.command {
    T {
      compile()
      fastOpt()
      indigoRun()() // Note the double parenthesis!
    }
  }

  def buildGameFull() = T.command {
    T {
      compile()
      fullOpt()
      indigoBuildFull()() // Note the double parenthesis!
    }
  }

  def runGameFull() = T.command {
    T {
      compile()
      fullOpt()
      indigoRunFull()() // Note the double parenthesis!
    }
  }
}