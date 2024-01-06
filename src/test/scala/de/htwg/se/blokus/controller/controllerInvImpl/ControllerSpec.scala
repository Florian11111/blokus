package de.htwg.se.blokus.controller.controllerInvImpl

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.blokus.controller.controllerInvImpl._
import de.htwg.se.blokus.models.fieldImpl.Field
import de.htwg.se.blokus.models.blockInvImpl.BlockInventory
import de.htwg.se.blokus.models.hoverBlockImpl.HoverBlock
import de.htwg.se.blokus.util.Observer
import scala.util.{Success, Failure}
import de.htwg.se.blokus.controller.*
import scala.util.{Try, Success}
import java.util.Random


class ControllerSpec extends AnyWordSpec with Matchers {
  "A Controller" when {
    "initialized" should {
      val controller = new Controller
      "start a game with correct parameters" in {
        controller.start(2, 10, 10)
        controller.getPlayerAmount() should be(2)
        controller.getWidth() should be(10)
        controller.getHeight() should be(10)
      }
    }

    "starting a game with different player amounts" should {
      "initialize the block inventory with 4 blocks for 1 player" in {
        val controller = new Controller
        controller.start(1, 10, 10)
        controller.blockInventory.getBlocks(0) should contain theSameElementsAs List(4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4)
      }

      "initialize the block inventory with 2 blocks for 2 players" in {
        val controller = new Controller
        controller.start(2, 10, 10)
        controller.blockInventory.getBlocks(0) should contain theSameElementsAs List(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
        controller.blockInventory.getBlocks(1) should contain theSameElementsAs List(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
      }

      "initialize the block inventory with 1 block for 3 players" in {
        val controller = new Controller
        controller.start(3, 10, 10)
        controller.blockInventory.getBlocks(0) should contain theSameElementsAs List(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
        controller.blockInventory.getBlocks(1) should contain theSameElementsAs List(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
        controller.blockInventory.getBlocks(2) should contain theSameElementsAs List(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
      }
    }

    "starting a game with different dimensions" should {
      "initialize the field with the specified width and height" in {
        val controller = new Controller
        controller.start(2, 5, 7)
        controller.getField().size should be(7)
        controller.getField()(0).size should be(5)
      }

      "require player amount to be between 1 and 4" in {
        val controller = new Controller
        assertThrows[AssertionError] {
          controller.start(0, 10, 10)
        }
        assertThrows[AssertionError] {
          controller.start(5, 10, 10)
        }
      }

      "require width and height to be greater than 0" in {
        val controller = new Controller
        assertThrows[AssertionError] {
          controller.start(2, 0, 10)
        }
        assertThrows[AssertionError] {
          controller.start(2, 10, 0)
        }
      }
    }

    "attempting to exit" should {
      val controller = new Controller
      "notify observers with ExitEvent" in {
        var notified = false
        controller.addObserver(new Observer[Event] {
          def update(event: Event): Unit = {
            notified = event == ExitEvent
          }
        })
        controller.exit()
        notified should be(true)
      }
    }

    "checking block placement validity" should {
      val controller = new Controller
      controller.start(2, 10, 10)

      "return true if placement is valid in a corner" in {
        controller.hoverBlock.setX(0)
        controller.hoverBlock.setY(0)
        controller.canPlace() should be(true)
      }

      "return true if placement is valid according to game rules" in {
        controller.field = Field.getInstance(10, 10)
        controller.hoverBlock.setX(0)
        controller.hoverBlock.setY(0)
        controller.canPlace() should be(true)
      }

      "return false if placement is invalid" in {
        controller.hoverBlock.setX(5)
        controller.hoverBlock.setY(5)
        controller.hoverBlock.setRotation(1)
        controller.canPlace() should be(false)
      }
    }

    "placing a block" should {
      val controller = new Controller
      controller.start(2, 10, 10)

      "succeed and notify observers with PlaceBlockEvent" in {
        var notified = false
        controller.addObserver(new Observer[Event] {
          def update(event: Event): Unit = {
            notified = event == PlaceBlockEvent
          }
        })
        controller.placeBlock() should be(Success(()))
        notified should be(true)
      }

      "succeed placing with place method" in {
        controller.setXandY(0,0)
        controller.place() shouldBe true
      }

      "not succeed placing with place method" in {
        controller.setXandY(0,0)
        controller.place() shouldBe false
      }
    }

    "calling TESTMETHOD" should {
      val controller = new Controller
      controller.start(2, 10, 10)

      "return an empty list of positions for a player" in {
        controller.TESTMETHOD(0) should be(empty)
      }

      "return an exception for an invalid player" in {
        val exception = intercept[java.lang.IllegalArgumentException] {
            controller.TESTMETHOD(2)
        }
        exception shouldBe an[java.lang.IllegalArgumentException]
        exception.getMessage shouldBe "Invalid player number: 2"
      }

      "return a not empty list of positions after doing smth" in {
        controller.blockInventory.setPosPositions(0, List((1,1),(1,1)))
        controller.TESTMETHOD(0) should not be(empty)
      }
    }

    "checking if a player is game over" should {
      val controller = new Controller
      controller.start(2, 10, 10)

      "return true if a player has no positions and no blocks" in {
        controller.blockInventory.useBlock(0, 0)
        controller.isGameOverPlayer(0) should be(true)
      }

      "return false if a player still has positions or blocks" in {
        controller.blockInventory.useBlock(0, 1)
        controller.blockInventory.setPosPositions(0, List((1, 0),(1,0),(1,1)))
        controller.isGameOverPlayer(0) should be(false)
      }
    }

    "switching players and checking for game over" should {
      val controller = new Controller
      controller.start(2, 10, 10)

      "notify observers with EndGameEvent if all players are game over" in {
        var notified = false
        controller.addObserver(new Observer[Event] {
          def update(event: Event): Unit = {
            notified = event == EndGameEvent
          }
        })
        controller.blockInventory.useBlock(0, 0)
        controller.blockInventory.useBlock(1, 0)
        controller.switchPlayerAndCheckGameOver() should be(true)
        notified should be(true)
      }

      "return false if not all players are game over" in {
        controller.blockInventory.useBlock(0, 0)
        controller.blockInventory.useBlock(1, 0)
        controller.blockInventory.setPosPositions(0, List((0, 0)))
        controller.switchPlayerAndCheckGameOver() should be(false)
      }
    }

    "calling getBlockAmount" should {
      "return a list of block amounts for each player" in {
        val controller = new Controller
        controller.start(2, 5, 5)
        controller.setXandY(0,0)
        controller.changeBlock(0)
        controller.placeBlock()
        controller.changeCurrentBlock(0)
        controller.setXandY(4, 4)
        controller.placeBlock()
        controller.getBlockAmount() should contain theSameElementsAs List(1, 1)
      }

      "return an empty list of block amounts for each player" in {
        val controller = new Controller
        controller.start(2,5,5)
        controller.getBlockAmount() should contain theSameElementsAs List(0, 0)
      }
    }

    "calling getBlocks" should {
      "return the list of blocks for the current player" in {
        val controller = new Controller
        controller.start(2, 10, 10)
        //controller.blockInventory.setPosPositions(0, List(2, 3))
        controller.getCurrentPlayer() should be(0)
        controller.getBlocks() should contain theSameElementsAs List(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
        controller.hoverBlock.setPlayer(1)
        controller.getBlocks() should contain theSameElementsAs List(2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
      }
    }

    "calling changeCurrentBlock" should {
      "change the current block type and notify observers" in {
        val controller = new Controller
        controller.start(2, 10, 10)
        val originalBlockType = controller.hoverBlock.getBlockType
        val newBlockType = originalBlockType + 1
        val result: Try[Unit] = controller.changeCurrentBlock(newBlockType)
        result shouldBe a [Success[Unit]]
        controller.hoverBlock.getBlockType should be(newBlockType)
        controller.hoverBlock.getX should be < 10 // Ensure getOutOfCorner was called
         }
      }

      "return a Try.Failure when changing to an invalid block type" in {
        val controller = new Controller
        controller.start(2, 10, 10)
        val invalidBlockType = -1
        controller.changeCurrentBlock(-1)
        val result: Try[Unit] = controller.changeCurrentBlock(invalidBlockType)
        result shouldBe a [Failure[Unit]]
      }
    }

    "calling getNextPlayer" should {
        "give out the next player correctly" in {
            val controller = new Controller
            controller.start(2, 10, 10)
            var current = controller.getCurrentPlayer()
            controller.getNextPlayer() shouldBe current + 1
        }
    }

    "calling getRotation" should {
        "give out the rotation correctly" in {
            val controller = new Controller
            controller.start(2, 10, 10)
            controller.changeBlock(3)
            controller.rotate()
            controller.rotate()
            var current = controller.getRotation()
            current shouldBe 2
        }
    }

    "calling addPotentialPositionsToInventory" should {
      "add potential positions to the inventory for the specified player" in {
        val controller = new Controller
        controller.start(2, 10, 10)
        controller.hoverBlock.setX(5)
        controller.hoverBlock.setY(5)
        controller.hoverBlock.setRotation(0)
        controller.hoverBlock.setMirrored(false)
        val blockInventory = new BlockInventory(2)
        blockInventory.setPosPositions(0, List((2, 2)))
        controller.blockInventory = blockInventory
        val positionsBefore = controller.blockInventory.getPosPositions(0)
        controller.addPotentialPositionsToInventory(0)
        val positionsAfter = controller.blockInventory.getPosPositions(0)
        positionsAfter should contain theSameElementsAs positionsBefore
      }
    }

    "getBlock()" should {
    "return a list of coordinates" in {
      val controller = new Controller
      controller.start(2,10,10)
      val result = controller.getBlock()
      result should contain theSameElementsAs List((5, 5)) // Passen Sie die erwarteten Koordinaten an
    }
  }

  "move(richtung)" should {
    val controller = new Controller
    controller.start(2, 10, 10)

    "return true if the move 0 is valid" in {
      val result = controller.move(0)
      result shouldBe true
    }

    "return true if the move 1 is valid" in {
      val result = controller.move(1)
      result shouldBe true
    }

    "return true if the move 2 is valid" in {
      val result = controller.move(2)
      result shouldBe true
    }

    "return true if the move 3 is valid" in {
      val result = controller.move(3)
      result shouldBe true
    }

    "return false if the move is invalid" in {
      controller.move(0)
      controller.move(0)
      controller.move(0)
      controller.move(0)
      controller.move(0)
      controller.move(0)
      controller.move(0)
      controller.move(0)
      controller.move(0)
      val result = controller.move(0)
      result shouldBe false
    }
  }

  "rotate()" should {
    "return true if the rotation is valid" in {
      val controller = new Controller
      controller.start(2, 10, 10)
      val result = controller.rotate()
      result shouldBe true
    }

    "return false if the rotation is invalid" in {
      val controller = new Controller
      controller.start(2, 10, 10)
      controller.changeBlock(2)
      controller.move(0)
      controller.move(0)
      controller.move(0)
      controller.move(0)
      controller.move(0)
      val result = controller.rotate()
      result shouldBe false
    }
  }

  "mirror()" should {
    "return true if the mirroring is valid" in {
      val controller = new Controller
      controller.start(2, 10, 10)
      val result = controller.mirror()
      result shouldBe true
    }

    "return false if the mirroring is invalid" in {
      val controller = new Controller
      controller.start(2, 5, 5)
      controller.changeBlock(3)
      controller.setXandY(3,0)
      val result = controller.mirror()
      result shouldBe false
    }
  }

  "performing undo" should {
      "return a Failure if the undoStack is empty" in {
        val controller = new Controller
        controller.start(2,10,10)
        val result = controller.undo()
        result shouldBe a[Failure[_]]
        result.failed.get shouldBe a[NoSuchElementException]
        result.failed.get.getMessage shouldBe "Nothing to undo!"
      }

       "execute the undo operation if the undoStack is not empty" in {
        val controller = new Controller
        controller.start(2, 5, 5)
        controller.changeCurrentBlock(0)
        controller.setXandY(0, 0)
        controller.placeBlock()

        val initialGameState = controller.getField()

        controller.changeCurrentBlock(0)
        controller.setXandY(4, 4)
        controller.placeBlock()

        val result = controller.undo()
        result shouldBe a[Success[_]]

        val currentState = controller.getField()
        currentState shouldEqual initialGameState
      }
    }

    "performing redo" should {
      "return a Failure if the redoStack is empty" in {
        val controller = new Controller
        controller.start(2,10,10)
        val result = controller.redo()
        result shouldBe a[Failure[_]]
        result.failed.get shouldBe a[NoSuchElementException]
        result.failed.get.getMessage shouldBe "Nothing to redo!"
      }

      "execute the redo operation if the redoStack is not empty" in {
        val controller = new Controller
        controller.start(2,10,10)
        controller.changeBlock(0)
        controller.move(1)
        controller.move(2)
        controller.placeBlock()

        val initialGameState = controller.getField()
        controller.undo()
        val result = controller.redo()
        result shouldBe a[Success[_]]

        val currentState = controller.getField()
        currentState shouldEqual initialGameState
      }
    }

    // Add more test cases for other Controller methods as needed
}