package de.htwg.se.blokus.models

import de.htwg.se.blokus.models.BlockInventoryInterface
import de.htwg.se.blokus.models.blockInvImpl.BlockInventory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BlockInventoryInterfaceSpec extends AnyWordSpec with Matchers {
  "BlockInventoryInterface" should {
    "return an instance of BlockInventoryInterface" in {
      val playerAmount = 4
      val initialCount = 2
      val blockInventoryInterface = BlockInventoryInterface.getInstance(playerAmount, initialCount)

      blockInventoryInterface shouldBe a[BlockInventoryInterface]
    }

    "return an instance of BlockInventoryInterface with default initialCount" in {
      val playerAmount = 3
      val blockInventoryInterface = BlockInventoryInterface.getInstance(playerAmount)

      blockInventoryInterface shouldBe a[BlockInventoryInterface]
    }
  }
}
