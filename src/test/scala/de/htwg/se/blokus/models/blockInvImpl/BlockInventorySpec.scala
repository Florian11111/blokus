package de.htwg.se.blokus.models.blockInvImpl

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scala.util.Random

class BlockInventorySpec extends AnyWordSpec with Matchers {
  "A BlockInventory" when {
    val playerAmount = 4
    val initialCount = 1
    val blockInventory = BlockInventory.getInstance(playerAmount, initialCount)
    val rand = new Random()

    "initialized" should {
      "have the correct playerAmount and initial block count" in {
        blockInventory.getBlocks(0) shouldEqual List.fill(21)(initialCount)
        blockInventory.firstBlock(0) shouldEqual true
        blockInventory.getPosPositions(0) shouldEqual List.empty
      }
    }

    "using getRandomBlock" should {
      "return a random block if available" in {
        blockInventory.getRandomBlock(0, rand) should not be empty
      }

      "return RuntimeException if no block is available" in {
        val emptyInventory = BlockInventory.getInstance(playerAmount, 0)
        val exception = intercept[java.lang.RuntimeException] {
            emptyInventory.getRandomBlock(0, rand)
        }
        exception shouldBe an[java.lang.RuntimeException]
        exception.getMessage shouldBe "No Block is available for Player 0."
      }

      "return IllegalArgumentException if no block is available" in {
        val emptyInventory = BlockInventory.getInstance(playerAmount, 0)
        val exception = intercept[java.lang.IllegalArgumentException] {
            emptyInventory.getRandomBlock(40, rand)
        }
        exception shouldBe an[java.lang.IllegalArgumentException]
        exception.getMessage shouldBe "Invalid player number: 40"
      }
    }

    "using isAvailable" should {
      "return true for available blocks" in {
        blockInventory.isAvailable(0, 0) shouldEqual true
      }

      "return ArrayIndexOutOfBoundsException for unavailable blocks" in {
        val exception = intercept[java.lang.ArrayIndexOutOfBoundsException] {
            blockInventory.isAvailable(0, 22)
        }
        exception shouldBe an[java.lang.ArrayIndexOutOfBoundsException]
      }

      "throw an exception for an invalid player number" in {
        val exception = intercept[java.lang.IllegalArgumentException] {
            blockInventory.isAvailable(-1, 0)
        }
        exception shouldBe an[java.lang.IllegalArgumentException]
      }
    }

    "using useBlock" should {
      "decrease block count and set isFirstBlock to false" in {
        val playerNumber = 0
        val blockNumber = 0
        blockInventory.useBlock(0, 1) shouldEqual List(1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
        blockInventory.firstBlock(playerNumber) shouldEqual false
      }

      "throw an exception for an invalid player number" in {
        val exception = intercept[java.lang.RuntimeException] {
            blockInventory.useBlock(-1, 0)
        }
        exception shouldBe an[java.lang.RuntimeException]
        exception.getMessage shouldBe "Invalid player number: -1"
      }

      "throw an exception for an invalid block number" in {
        val exception = intercept[java.lang.RuntimeException] {
            blockInventory.useBlock(0, 1)
        }
        exception shouldBe an[java.lang.RuntimeException]
        exception.getMessage shouldBe "Block 1 is not available for Player 0."
      }
    }

    "creating a deep copy" should {
      "return a new BlockInventory instance with the same state" in {
        val copy = blockInventory.deepCopy
        copy should not be theSameInstanceAs(blockInventory)
        copy.getBlocks(0) shouldEqual blockInventory.getBlocks(0)
      }
    }

    "Get blocks and error check" should {
        "throw an exception for an invalid player number" in {
        val exception = intercept[java.lang.IllegalArgumentException] {
            blockInventory.getBlocks(40)
        }
        exception shouldBe an[java.lang.IllegalArgumentException]
        exception.getMessage shouldBe "Invalid player number: 40"
      }
    }

    "getting and setting posPositions" should {
      "return the correct positions and update them" in {
        val playerNumber = 0
        val newPosPositions = List((1, 2), (3, 4))
        blockInventory.setPosPositions(playerNumber, newPosPositions)
        blockInventory.getPosPositions(playerNumber) shouldEqual newPosPositions
      }

      "throw an exception for an invalid player number bei set" in {
        val exception = intercept[java.lang.IllegalArgumentException] {
            blockInventory.setPosPositions(40, List.empty)
        }
        exception shouldBe an[java.lang.IllegalArgumentException]
        exception.getMessage shouldBe "Invalid player number: 40"
      }

      "throw an exception for an invalid player number bei get" in {
        val exception = intercept[java.lang.IllegalArgumentException] {
            blockInventory.getPosPositions(40)
        }
        exception shouldBe an[java.lang.IllegalArgumentException]
        exception.getMessage shouldBe "Invalid player number: 40"
      }
    }
  }
}
