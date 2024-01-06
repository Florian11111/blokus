package de.htwg.se.blokus.models

import de.htwg.se.blokus.models.HoverBlockInterface
import de.htwg.se.blokus.models.hoverBlockImpl.HoverBlock
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class HoverBlockInterfaceSpec extends AnyWordSpec with Matchers {
  "HoverBlockInterface" should {
    "return an instance of HoverBlockInterface" in {
      val x = 1
      val y = 2
      val playerAmount = 4
      val blockType = 3
      val rotation = 90
      val mirrored = true

      val hoverBlockInterface = HoverBlockInterface.getInstance(x, y, playerAmount, blockType, rotation, mirrored)

      hoverBlockInterface shouldBe a[HoverBlockInterface]
      hoverBlockInterface.getX shouldBe x
      hoverBlockInterface.getY shouldBe y
      hoverBlockInterface.getPlayer shouldBe 0
      hoverBlockInterface.getBlockType shouldBe blockType
      hoverBlockInterface.getRotation shouldBe rotation
      hoverBlockInterface.getMirrored shouldBe mirrored
    }
  }
}
