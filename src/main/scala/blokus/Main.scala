package blokus

import blokus.controller.Controller
import blokus.view.Tui

object Main {

  def main(args: Array[String]): Unit = {
    val controller = new Controller(4, 0, 20, 20)
    val tui = new Tui(controller)
    tui.inputLoop()
  }
}
