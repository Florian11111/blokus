package blokus.view

import blokus.controller.{Controller, ControllerEvent}
import blokus.util.Observer

// Define the different states of the TUI
private trait TuiState {
  def processInput(input: String, controller: Controller): Unit
}

private class DefaultTuiState extends TuiState {
  def processInput(input: String, controller: Controller): Unit = {
    input match {
      case "x" => System.exit(0)
      case "s" => controller.move(0)
      case "d" => controller.move(1)
      case "w" => controller.move(2)
      case "a" => controller.move(3)
      case "r" => controller.rotate()
      case "m" => controller.mirror()
      case "e" => controller.place(5) // Assuming 5 is a valid block type
      case "u" => controller.undo()
      case _   => println("Invalid command")
    }
  }
}

class Tui(controller: Controller) extends Observer[ControllerEvent] {
  private var currentState: TuiState = new DefaultTuiState()
  controller.addObserver(this)

  def processInput(input: String): Unit = {
    currentState.processInput(input, controller)
  }

  def display(): Unit = {
    println("Blokus Game")
    println(mergeFieldAndBlock().map(rowToString).mkString("\n"))
    println("\nPlayer: " + (controller.getCurrentPlayer() + 1))
    println("Controls: w/a/s/d to move, r to rotate, m to mirror, e to place, u to undo, x to exit")
  }

  def mergeFieldAndBlock(): Vector[Vector[Int]] = {
    val field = controller.getField()
        val block = controller.getBlock()

        val merged = field.zipWithIndex.map { case (row, rowIndex) =>
            row.zipWithIndex.map { case (fieldValue, columnIndex) =>
            block.find { case (x, y) =>
                x == columnIndex && y == rowIndex
            }.map(_ => if (fieldValue != -1) 11 else 10).getOrElse(fieldValue)
            }
        }
        merged
  }

  def rowToString(row: Vector[Int]): String = {
    row.map {
            case -1 => "+ "
            case 0 => "1 "
            case 1 => "2 "
            case 2 => "3 "
            case 3 => "4 "
            case 10 => "# "
            case 11 => "? "
            case _ => "? "
        }.mkString
  }

  override def update(event: ControllerEvent): Unit = {
    event match {
      case ControllerEvent.Update => display()
      case ControllerEvent.PlayerChange(player) =>
    }
  }

  def inputLoop(): Unit = {
    var continue = true
    display()
    while (continue) {
      val input = scala.io.StdIn.readLine()
      processInput(input)
      if (input == "x") continue = false
    }
  }
}
