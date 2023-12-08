package blokus.models

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class FieldSpec extends AnyWordSpec with Matchers {

  "Field Object" should {
    "create a new Field instance with the given dimensions" in {
      val width = 5
      val height = 5
      val field = Field.getInstance(width, height)

      field.width shouldBe width
      field.height shouldBe height
    }

    "return the same Field instance upon subsequent calls" in {
      val width = 5
      val height = 5
      val field1 = Field.getInstance(width, height)
      val field2 = Field.getInstance(width, height)

      field2 should be theSameInstanceAs(field1)
    }
  }

  "A Field" when {
    "initialized" should {
      val width = 5
      val height = 5
      val field = Field(width, height)

      "have the correct dimensions" in {
        field.width shouldBe width
        field.height shouldBe height
      }

      "be zero for an empty field" in {
        val emptyField = new Field(Vector.empty[Vector[Int]])
        emptyField.width shouldBe 0
      }

      "be empty" in {
        field.getFieldVector.foreach { row =>
          row should contain only -1
        }
      }
    }

    "checking for valid positions" should {
      val field = Field(5, 5)

      "return true for valid positions" in {
        field.isValidPosition(List((0, 0), (1, 1)), 2, 2) shouldBe true
      }

      "return false for invalid positions" in {
        field.isValidPosition(List((0, 0), (1, 6)), 2, 2) shouldBe false
      }
    }

    "checking game over conditions" should {
      "return true if the game is over" in {
        val field3 = Field(5, 5)
        // Assuming game over logic, for example, no more valid positions
        // Setup the field to a state that represents game over
        // Example:
        field3.placeBlock(List((0, 0), (1, 0)), 2, 2, 1)

        field3.isGameOver(List((1, 1)), 3, 3, 1) shouldBe true
      }

      "return false if the game is not over" in {
        var field2 = Field(5, 5)
        // Setup field to a state that does not represent game over
        // Example:
        field2 = field2.placeBlock(List((0, 0)), 2, 2, 1)

        field2.isGameOver(List((1, 1)), 1, 1, 1) shouldBe false
      }
    }

    "placing a block" should {
      "correctly update the field" in {
        val field = Field(5, 5)
        val newField = field.placeBlock(List((0, 0), (1, 0)), 2, 2, 1)

        newField.getFieldVector(2)(2) shouldBe 1
        newField.getFieldVector(2)(3) shouldBe 1
      }

      "throw an exception for invalid placement" in {
        val field = Field(5, 5)

        an[IllegalArgumentException] should be thrownBy field.placeBlock(List((0, 0), (6, 6)), 2, 2, 1)
      }
    }

    "checking for valid placements" should {
      "return true for a valid placement" in {
        val field = Field(5, 5)
        // Example setup where placement is valid
        // Example:
        field.placeBlock(List((0, 0)), 1, 1, 1)

        field.isValidPlace(List((1, 1)), 2, 2, 1) shouldBe true
      }

      "return false for an invalid placement" in {
        var field6 = Field(5, 5)
        // Place a block at (2, 2) for player 1
        field6 = field6.placeBlock(List((0, 0)), 2, 2, 1)

        // Attempt to place another block at the same position for player 2
        // This should be invalid due to overlap
        field6.isValidPlace(List((0, 0)), 2, 2, 2) shouldBe false
      }
    }

    "copying the field" should {
      "create a deep copy" in {
        val field = Field(5, 5)
        val copyField = field.copy()

        copyField should not be theSameInstanceAs(field)
        copyField.getFieldVector shouldBe field.getFieldVector
      }
    }
  }
}
