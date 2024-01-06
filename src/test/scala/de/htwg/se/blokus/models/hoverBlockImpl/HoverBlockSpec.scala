package de.htwg.se.blokus.models.hoverBlockImpl

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class HoverBlockSpec extends AnyWordSpec with Matchers {

  "A HoverBlock" when {
    "created" should {
      "initialize the fields correctly" in {
        var x = 2
        var y = 3
        var playerAmount = 4
        var blockType = 0
        var rotation = 1
        var mirrored = false

        var hoverBlock = HoverBlock.createInstance(x, y, playerAmount, blockType, rotation, mirrored)

        hoverBlock.getX shouldEqual x
        hoverBlock.getY shouldEqual y
        hoverBlock.getPlayer shouldEqual 0
        hoverBlock.getBlockType shouldEqual blockType
        hoverBlock.getRotation shouldEqual rotation
        hoverBlock.getMirrored shouldEqual mirrored
      }
    }

    "setX" should {
      "update the X coordinate and return the previous X coordinate" in {
        var hoverBlock = HoverBlock.createInstance(2, 3, 4, 0, 1, false)

        var prevX = hoverBlock.setX(5)

        hoverBlock.getX shouldEqual 5
        prevX shouldEqual 2
      }
    }

    "setY" should {
      "update the Y coordinate and return the previous Y coordinate" in {
        var hoverBlock = HoverBlock.createInstance(2, 3, 4, 0, 1, false)

        var prevY = hoverBlock.setY(5)

        hoverBlock.getY shouldEqual 5
        prevY shouldEqual 3
      }
    }

    "setPlayer" should {
      "update the current player and return the previous player" in {
        var hoverBlock = HoverBlock.createInstance(2, 3, 4, 0, 1, false)

        var prevPlayer = hoverBlock.setPlayer(1)

        hoverBlock.getPlayer shouldEqual 1
        prevPlayer shouldEqual 0
      }
    }

    "setBlockType" should {
      "update the block type and return the previous block type" in {
        var hoverBlock = HoverBlock.createInstance(2, 3, 4, 0, 1, false)

        var prevBlockType = hoverBlock.setBlockType(2)

        hoverBlock.getBlockType shouldEqual 2
        prevBlockType shouldEqual 0
      }
    }

    "setRotation" should {
      "update the rotation and return the previous rotation" in {
        var hoverBlock = HoverBlock.createInstance(2, 3, 4, 0, 1, false)

        var prevRotation = hoverBlock.setRotation(3)

        hoverBlock.getRotation shouldEqual 3
        prevRotation shouldEqual 1
      }
    }

    "setMirrored" should {
      "update the mirrored flag and return the previous mirrored flag" in {
        var hoverBlock = HoverBlock.createInstance(2, 3, 4, 0, 1, false)

        var prevMirrored = hoverBlock.setMirrored(true)

        hoverBlock.getMirrored shouldEqual true
        prevMirrored shouldEqual false
      }
    }

    "getOutOfCorner" should {
    "return true and adjust coordinates if the block is in the top-left corner" in {
        var hoverBlock = HoverBlock.createInstance(0, 0, 4, 0, 1, false)
        var height = 5
        var width = 5

        var result = hoverBlock.getOutOfCorner(height, width)

        result shouldEqual true
        hoverBlock.getX shouldEqual 2
        hoverBlock.getY shouldEqual 0
    }

    "return true and adjust coordinates if the block is in the top-right corner" in {
        var hoverBlock = HoverBlock.createInstance(3, 0, 4, 0, 1, false)
        var height = 5
        var width = 5

        var result = hoverBlock.getOutOfCorner(height, width)

        result shouldEqual true
        hoverBlock.getX shouldEqual 3
        hoverBlock.getY shouldEqual 2
    }

    "return true and adjust coordinates if the block is in the bottom-left corner" in {
        var hoverBlock = HoverBlock.createInstance(0, 3, 4, 0, 1, false)
        var height = 5
        var width = 5

        var result = hoverBlock.getOutOfCorner(height, width)

        result shouldEqual true
        hoverBlock.getX shouldEqual 2
        hoverBlock.getY shouldEqual 3
    }

    "return true and adjust coordinates if the block is in the bottom-right corner" in {
        var hoverBlock = HoverBlock.createInstance(4, 4, 4, 0, 1, false)
        var height = 5
        var width = 5

        var result = hoverBlock.getOutOfCorner(height, width)

        result shouldEqual true
        hoverBlock.getX shouldEqual 3
        hoverBlock.getY shouldEqual 4
    }

    "return true and adjust coordinates if the block is at the bottom edge" in {
        var hoverBlock = HoverBlock.createInstance(2, 4, 4, 0, 1, false)
        var height = 5
        var width = 5

        var result = hoverBlock.getOutOfCorner(height, width)

        result shouldEqual true
        hoverBlock.getX shouldEqual 2
        hoverBlock.getY shouldEqual 3
    }

    "return false if the block is not in the corner" in {
        var hoverBlock = HoverBlock.createInstance(2, 3, 4, 0, 1, false)
        var height = 5
        var width = 5

        var result = hoverBlock.getOutOfCorner(height, width)

        result shouldEqual false
        hoverBlock.getX shouldEqual 2
        hoverBlock.getY shouldEqual 3
    }
    }
  }
}
