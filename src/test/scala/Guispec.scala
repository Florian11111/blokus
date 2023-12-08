import blokus.controller.Controller
import blokus.controller.ControllerEvent
import blokus.util.Observer
import blokus.view.Gui
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scalafx.scene.paint.Color

class GuiSpec extends AnyWordSpec with Matchers {

  "Gui" when {
    "creating a scene" should {
      "initialize GUI components correctly" in {
        val controller = new Controller(4, 0, 10, 10)
        val gui = new Gui(controller, 800, 600)

        gui.boardPane should not be null
        gui.currentPlayerLabel should not be null
        gui.blocksLabel should not be null
        gui.numberTextField should not be null
      }
    }

    "handling button actions" should {
      "perform the correct actions when buttons are clicked" in {
        val controller = new Controller(4, 0, 10, 10)
        val gui = new Gui(controller, 800, 600)

        gui.handleButtonAction(0) // Hoch
        // Assertions for the action result
        controller.getCurrentPlayer() shouldEqual 1 // Adjust to your actual logic

        gui.handleButtonAction(1) // Runter
        // Assertions for the action result
        controller.getCurrentPlayer() shouldEqual 2 // Adjust to your actual logic

        gui.handleButtonAction(2) // Links
        // Assertions for the action result
        controller.getCurrentPlayer() shouldEqual 3 // Adjust to your actual logic

        gui.handleButtonAction(3) // Rechts
        // Assertions for the action result
        controller.getCurrentPlayer() shouldEqual 4 // Adjust to your actual logic

        gui.handleButtonAction(4) // Spiegeln
        // Assertions for the action result
        // Add more assertions as needed

        gui.handleButtonAction(5) // Drehen
        // Assertions for the action result
        // Add more assertions as needed

        gui.handleButtonAction(6) // Setzten
        // Assertions for the action result
        // Add more assertions as needed

        gui.handleButtonAction(7) // Undo
        // Assertions for the action result
        // Add more assertions as needed

        gui.handleButtonAction(8) // BlockWechseln
        // Assertions for the action result
        // Add more assertions as needed
      }
    }

    "merging field and block" should {
      "correctly merge the field and block data" in {
        val controller = new Controller(4, 0, 10, 10)
        val gui = new Gui(controller, 800, 600)

        // Mock field and block data as needed
        // ...

        val mergedData = gui.mergeFieldAndBlock()
        // Assertions for mergedData correctness
        // Add more assertions as needed
      }
    }

    "updating the board" should {
      "correctly update the board based on merged data" in {
        val controller = new Controller(4, 0, 10, 10)
        val gui = new Gui(controller, 800, 600)

        // Mock merged data as needed
        // ...

        gui.updateBoard()
        // Assertions for the updated board correctness
        // Add more assertions as needed
      }
    }

    "getting fill color" should {
      "return the correct color based on the input value" in {
        val controller = new Controller(4, 0, 10, 10)
        val gui = new Gui(controller, 800, 600)

        val color1 = gui.getFillColor(-1)
        color1 shouldEqual Color.White

        val color2 = gui.getFillColor(0)
        color2 shouldEqual Color.Red

        // Add more assertions for other possible input values
      }
    }

    "updating labels" should {
      "correctly update the labels with the expected text and styles" in {
        val controller = new Controller(4, 0, 10, 10)
        val gui = new Gui(controller, 800, 600)

        // Mock data or state changes as needed
        // ...

        gui.updateLabels()
        // Assertions for label updates correctness
        // Add more assertions as needed
      }
    }
  }
}
