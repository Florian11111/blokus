import blokus.models.{HoverBlock, Field, Block}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class HoverBlockSpec extends AnyWordSpec with Matchers {

  "HoverBlock" should {

    val playerAmount = 4
    val firstBlockType = 1
    val hoverBlock = new HoverBlock(playerAmount, firstBlockType)
    val testField = Field(10, 10) // Assuming a 10x10 field

    "initialize correctly" in {
      hoverBlock.getX() shouldBe 2
      hoverBlock.getY() shouldBe 2
      hoverBlock.getRotation() shouldBe 0
      hoverBlock.getCurrentPlayer shouldBe 0
      hoverBlock.getBlock() shouldBe Block.createBlock(firstBlockType, 0, false).map { case (x, y) => (x + 2, y + 2) }
    }

    "change player correctly" in {
      for (i <- 1 until playerAmount) {
        hoverBlock.changePlayer() shouldBe i
      }
      hoverBlock.changePlayer() shouldBe 0 // Wrap around to first player
    }

    "move correctly" in {
      hoverBlock.move(testField, 0) shouldBe true // Move down
      hoverBlock.getY() shouldBe 3

      hoverBlock.move(testField, 1) shouldBe true // Move right
      hoverBlock.getX() shouldBe 3

      hoverBlock.move(testField, 2) shouldBe true // Move up
      hoverBlock.getY() shouldBe 2

      hoverBlock.move(testField, 3) shouldBe true // Move left
      hoverBlock.getX() shouldBe 2

      hoverBlock.move(testField, 4) shouldBe false // Invalid direction
    }

    "rotate correctly" in {
      val initialRotation = hoverBlock.getRotation()

      // Rotate through all four directions
      for (i <- 1 to 4) {
        hoverBlock.rotate(testField) shouldBe true
        hoverBlock.getRotation() shouldBe (initialRotation + i) % 4
      }
     }

    "mirror correctly" in {
      hoverBlock.mirror(testField) shouldBe true
      // Test mirroring and ensure it toggles the mirrored state
      // Ensure mirroring is not possible when it would place block out of bounds
    }

    "set block correctly" in {
      val newBlockType = 2
      val newField = hoverBlock.place(testField, newBlockType)
      // Assert the block is set correctly on the field
      // Assert hoverBlock's state is reset to initial values
    }

    "determine if a block can be placed correctly" in {
      hoverBlock.canPlace(testField) shouldBe true
    }
  }

  "HoverBlock.getInstance" should {
    "return the same instance for the same playerAmount and firstBlock" in {
      val playerAmount = 4
      val firstBlock = 0

      val instance1 = HoverBlock.getInstance(playerAmount, firstBlock)
      val instance2 = HoverBlock.getInstance(playerAmount, firstBlock)

      assert(instance1 eq instance2) // Überprüfen, ob beide Instanzen identisch sind
      assert(instance1.hashCode() == instance2.hashCode()) // Überprüfen, ob hashCode gleich ist
    }
  }
}