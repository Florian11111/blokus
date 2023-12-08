package blokus

import blokus.controller.Controller
import scala.concurrent.{Future, ExecutionContext, Await}
import scala.concurrent.duration.Duration

import blokus.view.Tui
import blokus.view.Gui

object Main {
  def main(args: Array[String]): Unit = {
    val controller = new Controller(4, 0, 20, 20)
    val tui = new Tui(controller)

    // Starte die TUI
    tui.inputLoop()
    // Warten, bis die GUI initialisiert ist, bevor die TUI gestartet wird
  }
}
