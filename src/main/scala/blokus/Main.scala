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
    val gui = new Gui(controller, 500, 650)

    // Hier wird die GUI in einem separaten Thread gestartet
    implicit val context: ExecutionContext = scala.concurrent.ExecutionContext.global
    val guiFuture: Future[Unit] = Future {
      gui.main(Array[String]())
    }

    // Starte die TUI
    tui.inputLoop()
    // Warten, bis die GUI initialisiert ist, bevor die TUI gestartet wird
    Await.result(guiFuture, Duration.Inf)

  }
}
