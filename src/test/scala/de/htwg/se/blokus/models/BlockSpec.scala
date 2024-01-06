package de.htwg.se.blokus.models

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BlockSpec extends AnyWordSpec with Matchers {

  "A Block" when {
    "using blockBaseForms" should {
      "contain all expected base forms" in {
        val expectedBaseForms: Array[List[(Int, Int)]] = Array(
          List((0, 0)), List((0, 0), (1, 0)), List((-1, 0), (0, 0), (1, 0)),
          List((0, 0), (1, 0), (0, 1)), List((-1, 0), (0, 0), (1, 0), (2, 0)),
          List((-1, 0), (-1, 1), (0, 0), (1, 0)), List((-1, 0), (0, 0), (1, 0), (0, 1)),
          List((0, 0), (1, 0), (0, 1), (1, 1)), List((0, 0), (-1, 0), (0, 1), (1, 1)),
          List((-2, 0), (-1, 0), (0, 0), (1, 0), (2, 0)), List((-1, 1), (-1, 0), (0, 0), (1, 0), (1, 1)),
          List((-1, 0), (0, 0), (1, 0), (0, 1), (1, 1)), List((-1, 0), (0, 0), (1, 0), (2, 0), (0, 1)),
          List((-2, 0), (-1, 0), (0, 0), (0, 1), (1, 1)), List((-2, 0), (-1, 0), (0, 0), (1, 0), (1, 1)),
          List((0, 0), (0, -1), (1, 0), (0, 1), (-1, 0)), List((-1, -1), (0, -1), (0, 0), (0, 1), (1, 1)),
          List((-1, -1), (0, -1), (0, 0), (1, 0), (1, 1)), List((-1, -1), (0, -1), (1, -1), (0, 0), (0, 1)),
          List((-1, -1), (0, -1), (0, 0), (1, 0), (0, 1)), List((-1, -1), (0, -1), (1, -1), (1, 0), (1, 1))
        )

        Block.blockBaseForms should contain theSameElementsAs expectedBaseForms
      }
    }

    "created with createBlock" should {
      "return a Block with the correct base form, rotation, and mirrored flag" in {
        val blockType = 1
        val rotation = 2
        val mirrored = true

        val block = Block.createBlock(blockType, rotation, mirrored)

        block.baseForm shouldEqual List((0, 0), (-1, 0))
        block.corners shouldEqual List((-2, 1), (-2, -1), (1, 1), (1, -1))
        block.edges shouldEqual List((-2, 0), (-1, -1), (-1, 1), (1, 0), (0, -1), (0, 1))
      }
    }
  }

  "eckenUndKanten" should {
    "return the correct corners and edges for a given block" in {
      val block = List((0, 0), (1, 0), (0, 1))

      val (corners, edges) = Block.eckenUndKanten(block)

      corners shouldEqual List((1, 2), (-1, 2), (2, 1), (2, -1), (-1, -1))
      edges shouldEqual List((-1, 1), (1, 1), (0, 0), (0, 2), (2, 0), (1, -1), (-1, 0), (1, 0), (0, -1), (0, 1))
    }
  }
}
