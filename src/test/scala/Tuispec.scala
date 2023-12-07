package blokus.view

import blokus.controller.{Controller, ControllerEvent}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._
import blokus.util.Observer
import blokus.models.Block

// Test Observer to capture state changes and outputs
class TestObserver extends Observer[ControllerEvent] {
  var latestOutput: String = ""
  var latestEvent: ControllerEvent = _

  override def update(event: ControllerEvent): Unit = {
    latestEvent = event
    // Here, you would capture the output of Tui.display or other relevant methods
    // For example, you might adapt Tui to allow capturing its printed output
  }
}

class TuiSpec extends AnyWordSpec {
  "A Tui" when {
    "initialized with a controller" should {
      val controller = new Controller(2, 5, 5, 5) // Setup the controller
      val tui = new Tui(controller) // Instantiate Tui with the controller
      val testObserver = new TestObserver() // Test observer to capture Tui output
      controller.addObserver(testObserver) // Add test observer to controller

      "display the initial state" in {
        val lmao1 = Vector(
          Vector(-1, -1, -1, -1, -1),
          Vector(-1, -1, -1, -1, -1),
          Vector(-1, 10, 10, 10, -1),
          Vector(-1, 10, -1, -1, -1),
          Vector(-1, -1, -1, -1, -1)
        )
        tui.mergeFieldAndBlock() shouldBe lmao1
      }

      "update display on controller event" in {
        // Simulate a controller event, such as a move or rotate
        controller.move(1) // Example: move the block
        val lmao = Vector(
          Vector(-1, -1, -1, -1, -1),
          Vector(-1, -1, -1, -1, -1),
          Vector(-1, -1, 10, 10, 10),
          Vector(-1, -1, 10, -1, -1),
          Vector(-1, -1, -1, -1, -1)
        )
        tui.mergeFieldAndBlock() shouldBe lmao
      }

      "correctly convert a row vector to its string representation" in {
        val row = Vector(-1, 0, 1, 2, 3, 10, 11, 4) // Updated example row vector
        tui.rowToString(row) shouldBe "+ 1 2 3 4 # ? ? " // Updated expected output
      }

      "correctly merge field and block with the player number" in {
        controller.changePlayer(1)
        controller.move(2)
        controller.move(2)
        controller.place(5)

        val expectedMergedField = Vector(
            Vector(-1, -1, 1, 1, 1),
            Vector(-1, -1, 1, -1, -1),
            Vector(-1, 10, 10, 10, -1),
            Vector(-1, 10, -1, -1, -1),
            Vector(-1, -1, -1, -1, -1)
        )
        tui.mergeFieldAndBlock() shouldBe expectedMergedField
      }

      "handle user input correctly" in {
        //controller.move(0) shouldBe
        //controller.move(3) shouldBe
        //controller.move(2) shouldBe
        //controller.move(1) shouldBe
        //controller.rotate() shouldBe
        //controller.mirror() shouldBe
        //controller.place(2) shouldBe
        // Simulate user inputs such as 'w', 'a', 's', 'd', 'r', 'm'
        // You can do this by calling the methods on the controller that these inputs would trigger
        // For each input, check that the Tui's state or output updates as expected
      }
    }
  }
}
