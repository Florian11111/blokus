package blokus.models

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class BlockInventorySpec extends AnyWordSpec with Matchers {
  "A BlockInventory" when {

    "initialized" should {
      "have the correct initial counts for each block for each player" in {
        val playerAmount = 4
        val initialCount = 1
        val blockInventory = new BlockInventory(playerAmount, initialCount)

        for (player <- 0 until playerAmount) {
          blockInventory.getBlocks(player).foreach { count =>
            count shouldBe initialCount
          }
        }
      }
    }

    "getting blocks for a player" should {
      "return the correct list of blocks" in {
        val blockInventory = new BlockInventory(4, 1)
        blockInventory.getBlocks(1).size shouldBe 20
      }

      "throw an exception for an invalid player number" in {
        val blockInventory = new BlockInventory(4, 1)
        an[IllegalArgumentException] should be thrownBy blockInventory.getBlocks(-1)
        an[IllegalArgumentException] should be thrownBy blockInventory.getBlocks(5)
      }
    }

    "getting and setting complete inventory" should {
      "correctly return and set the entire inventory" in {
        val blockInventory = new BlockInventory(4, 1)
        val originalState = blockInventory.getCompleteInventory()

        // Modify the inventory
        blockInventory.useBlock(1, 1)
        blockInventory.setCompleteInventory(originalState)

        blockInventory.getBlocks(1)(1) shouldBe 1
      }
    }

    "using a block" should {
      "decrease the block count" in {
        val blockInventory = new BlockInventory(4, 2)
        blockInventory.useBlock(1, 1)
        blockInventory.getBlocks(1)(1) shouldBe 1
      }

      "throw an exception if the block is not available" in {
        val blockInventory = new BlockInventory(4, 1)
        blockInventory.useBlock(1, 1)
        an[RuntimeException] should be thrownBy blockInventory.useBlock(1, 1)
      }
    }

    "getting a random block" should {
      "return a block if available" in {
        val blockInventory = new BlockInventory(4, 1)
        blockInventory.getRandomBlock(1).isDefined shouldBe true
      }

      "throw an exception if no block is available" in {
        val blockInventory = new BlockInventory(4, 0)
        an[RuntimeException] should be thrownBy blockInventory.getRandomBlock(1)
      }
    }

    "checking block availability" should {
      "return true if the block is available" in {
        val blockInventory = new BlockInventory(4, 1)
        blockInventory.isAvailable(1, 1) shouldBe true
      }

      "return false if the block is not available" in {
        val blockInventory = new BlockInventory(4, 0)
        blockInventory.isAvailable(1, 1) shouldBe false
      }

      "throw an exception for an invalid player number" in {
        val blockInventory = new BlockInventory(4, 1)
        an[IllegalArgumentException] should be thrownBy blockInventory.isAvailable(-1, 1)
        an[IllegalArgumentException] should be thrownBy blockInventory.isAvailable(5, 1)
      }
    }

    "getting available blocks" should {
      "return a list of all available blocks" in {
        val blockInventory = new BlockInventory(4, 1)
        blockInventory.getAvailableBlocks(1).size shouldBe 20
      }
    }
  }
}
