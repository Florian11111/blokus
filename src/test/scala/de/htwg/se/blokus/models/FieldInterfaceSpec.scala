package de.htwg.se.blokus.models

import de.htwg.se.blokus.models.FieldInterface
import de.htwg.se.blokus.models.fieldImpl.Field
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class FieldInterfaceSpec extends AnyWordSpec with Matchers {
  "FieldInterface" should {
    "return an instance of FieldInterface with the specified width and height" in {
      val width = 5
      val height = 5

      val fieldInterface = FieldInterface.getInstance(width, height)

      fieldInterface shouldBe a[FieldInterface]
      fieldInterface.width shouldBe width
      fieldInterface.height shouldBe height
    }

    "initialize the field with default values (-1)" in {
      val width = 3
      val height = 3

      val fieldInterface = FieldInterface.getInstance(width, height)

      val fieldVector = fieldInterface.getFieldVector
      fieldVector.foreach(row => row shouldBe Vector.fill(width)(-1))
    }
  }
}
