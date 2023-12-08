package blokus.controller

import blokus.models.Field
import blokus.models.HoverBlock
import blokus.models.BlockInventory
import blokus.util.{Observable, Observer}

import scala.util.{Try, Success, Failure}

class Controller(playerAmount: Int, firstBlock: Int, width: Int, height: Int) extends Observable[ControllerEvent] {
    assert(playerAmount >= 1 && playerAmount < 5)

    var field = Field(width, height)
    var hoverBlock = HoverBlock(playerAmount, firstBlock)
    var blockInventory = new BlockInventory(playerAmount)

    def getWidth(): Int = width
    def getHeight(): Int = height

    def place(newBlock: Int): Try[Unit] = {
        execute(SetBlockCommand(this, field, getCurrentPlayer(), hoverBlock.getCurrentBlock))
    }

    def place_2(neuerTyp: Int): Unit = {
        field = hoverBlock.place(field, neuerTyp)
        notifyObservers(ControllerEvent.Update)
    }

    def getBlocks(): List[Int] = blockInventory.getBlocks(getCurrentPlayer())

    def changeCurrentBlock(newBlock: Int): Try[Unit] = Try {
        hoverBlock.currentBlockTyp = newBlock
        hoverBlock.getOutOfCorner(height, width)
        notifyObservers(ControllerEvent.Update)
    }


    def getCurrentPlayer(): Int = hoverBlock.getCurrentPlayer
    def getField(): Vector[Vector[Int]] = field.getFieldVector

    def getBlock(): List[(Int, Int)] = hoverBlock.getBlock()

    def setCurrentBlock(newBlock: Int): Int = {
        val temp = hoverBlock.setCurrentBlock(newBlock)
        notifyObservers(ControllerEvent.Update)
        temp
    }

    def move(richtung: Int): Boolean = {
        val moved = hoverBlock.move(field, richtung)
        if (moved) {
            notifyObservers(ControllerEvent.Update)
        }
        moved
    }

    def rotate(): Boolean = {
        val rotated = hoverBlock.rotate(field)
        if (rotated) {
            notifyObservers(ControllerEvent.Update)
        }
        rotated
    }

    def mirror(): Boolean = {
        val mirrored = hoverBlock.mirror(field)
        if (mirrored) {
            notifyObservers(ControllerEvent.Update)
        }
        mirrored
    }

    def canPlace(): Boolean = {
        hoverBlock.canPlace(field)
    }

    def changeBlock(neuerBlock: Int): Int = {
        val currentBlock = hoverBlock.currentBlockTyp
        hoverBlock.currentBlockTyp = neuerBlock
        currentBlock
    }
    def getRotation(): Int = hoverBlock.getRotation()

    def nextPlayer(): Int = {
        val currentPlayer = hoverBlock.changePlayer()
        notifyObservers(ControllerEvent.PlayerChange(currentPlayer))
        currentPlayer
    }

    def changePlayer(newPlayer: Int): Try[Unit] = {
        Try {
            val currentPlayer = hoverBlock.setPlayer(newPlayer)
            notifyObservers(ControllerEvent.PlayerChange(currentPlayer))
        }
    }

    private var undoStack: List[Command] = List()
    private var redoStack: List[Command] = List()

    private def execute(command: Command): Try[Unit] = {
		undoStack = command :: undoStack
		redoStack = List()
		command.execute()
	}

    def undo(): Try[Unit] = {
        undoStack match {
            case Nil => {
                Failure(new NoSuchElementException("Nothing to undo!"))
            }
            case head :: tail => {
                head.undo()
                undoStack = tail
                redoStack = head :: redoStack
                Success(())
            }
        }
    }

    def redo(): Try[Unit] = {
		redoStack match {
			case Nil => Failure(new NoSuchElementException("Nothing to redo!"))
			case head :: tail => {
				head.redo()
				redoStack = tail
				undoStack = head :: undoStack
				Success(())
			}
		}
	}

    private trait Command {
		def execute(): Try[Unit]
		def undo(): Unit
		def redo(): Try[Unit]
	}

    private class SetBlockCommand(controller: Controller, newField: Field, player: Int, blockTyp: Int) extends Command {
    private val originalField = controller.field
    private val originalInventoryState = controller.blockInventory.getCompleteInventory()

    override def execute(): Try[Unit] = Try {
        controller.place_2(blockTyp)
    }

    override def undo(): Unit = {
        controller.field = originalField
        controller.blockInventory.setCompleteInventory(originalInventoryState)
        controller.changePlayer(player)
        notifyObservers(ControllerEvent.Update)
    }

    override def redo(): Try[Unit] = execute()
}

}


sealed trait ControllerEvent
object ControllerEvent {
    case object Update extends ControllerEvent
    case class PlayerChange(player: Int) extends ControllerEvent
}
