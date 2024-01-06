package de.htwg.se.blokus.view

import de.htwg.se.blokus.controller.controllerInvImpl.Controller
import de.htwg.se.blokus.controller.GameController
import de.htwg.se.blokus.controller.*
import de.htwg.se.blokus.util.Observer

class Tui(controller: GameController) extends Observer[Event] {
    controller.addObserver(this)
    var gameisStarted = false
    var isGameOver = false

    def clearTerminal(): Unit = {
        val os = System.getProperty("os.name").toLowerCase()
        if (os.contains("win")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor()
        } else {
            print("\u001b[H\u001b[2J")
            System.out.flush()
        }
    }

    def mergeFieldAndBlock(): Vector[Vector[Int]] = {
        val field = controller.getField()
        val block = controller.getBlock()

        field.zipWithIndex.map { case (row, rowIndex) =>
            row.zipWithIndex.map { case (fieldValue, columnIndex) =>
            block.find { case (x, y) =>
                x == columnIndex && y == rowIndex
            }.map(_ => if (fieldValue != -1) 11 else 10).getOrElse(fieldValue)
            }
        }
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

    def display(): Unit = {
        println(mergeFieldAndBlock().map(rowToString).mkString("\n"))
    }

    def displayControlls(): Unit = {
        println("\nBloecke:")
        print(controller.getBlocks())
        println("\nPlayer:")
        print(controller.getCurrentPlayer() + 1)
        println("\nSteuerung:")
        println("w/a/s/d: Block bewegen")
        println("r: Block rotieren")
        println("m: Block spiegeln")
        println("e: Block platzieren")
        println("u: Undo")
        println("x: Beenden")
    }

    override def update(event: Event): Unit = {
        event match {
            case StartGameEvent => {
                gameisStarted = true
                isGameOver = false
            }
            case EndGameEvent => {
                gameisStarted = false
                isGameOver = true
                printEndGame()
            }
            case ExitEvent => System.exit(0)
            case _ =>
        }
        if (gameisStarted) {
            clearTerminal()
            display()
            displayControlls()
        }
    }

    def printEndGame(): Unit = {
        clearTerminal()
        println("Game Over!")
        val results = controller.getBlockAmount()
        for (i <- 0 until results.size) {
            println("Player " + (i + 1) + ": " + results(i))
        }
        val winner = results.indexOf(results.max)
        println("Player " + (winner + 1) + " has won!")
        println("new Game? (n)")
        println("Close? (x)")
    }


    def inputLoop(): Unit = {
        var continue = true
        while (continue) {
        if (isGameOver) {
            val input = scala.io.StdIn.readLine()
            if (input == "x") {
                controller.exit()
            } else if (input == "n") {
                controller.start(controller.getPlayerAmount(), controller.getWidth(), controller.getHeight())
            }
        }

        if (!gameisStarted) {
            println("Blokus")
            println("Anzahl Spieler: ")
            val playerCount = scala.io.StdIn.readInt()
            assert(playerCount >= 1 && playerCount <= 4)
            println("Spielfeldgroesse X: ")
            val fieldSizeX = scala.io.StdIn.readInt()
            println("Spielfeldgroesse Y: ")
            val fieldSizeY = scala.io.StdIn.readInt()
            controller.start(playerCount, fieldSizeX, fieldSizeY)
        }
        print("test?")
        try {
            val input = scala.io.StdIn.readLine()
            input match {
                case "x" => controller.exit()
                case "w" => controller.move(2)
                case "d" => controller.move(1)
                case "s" => controller.move(0)
                case "a" => controller.move(3)
                case "u" => controller.undo()
                case "r" => controller.rotate()
                case "m" => controller.mirror()
                case "e" => {
                    if (controller.canPlace()) {
                        controller.placeBlock()
                    } else {
                        println("Kann nicht an dieser Stelle Platziert werden!")
                    }
                }
                case _ =>
            }

        } finally {
            // Reset any terminal configurations if needed
        }
    }
    }
}

