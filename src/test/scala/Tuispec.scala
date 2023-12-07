package blokus.view

import blokus.controller.{Controller, ControllerEvent}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import blokus.util.Observer

class TuiSpec extends AnyFlatSpec with Matchers {

  class TestController extends Controller(4, 0, 10, 10)

  class TestObserver extends Observer[ControllerEvent] {
    var events: List[ControllerEvent] = Nil

    override def update(event: ControllerEvent): Unit = {
      events = event :: events
    }
  }

  "A Tui" should "process valid inputs" in {
    val controller = new TestController
    val tui = new Tui(controller)
    val observer = new TestObserver
    controller.addObserver(observer)

    tui.processInput("s") shouldBe ()
    tui.processInput("d") shouldBe ()
    tui.processInput("w") shouldBe ()
    tui.processInput("a") shouldBe ()
    tui.processInput("r") shouldBe ()
    tui.processInput("m") shouldBe ()
    tui.processInput("e") shouldBe ()
    tui.processInput("u") shouldBe ()

    // Hier überprüfen, ob die Controller-Events ordnungsgemäß empfangen wurden
    observer.events should contain(ControllerEvent.Update)
  }

  it should "display the game" in {
    val controller = new TestController
    val tui = new Tui(controller)

    tui.display()
  }

  it should "merge field and block" in {
    val controller = new TestController
    val tui = new Tui(controller)

    val merged = tui.mergeFieldAndBlock()
    merged should have length controller.getField().size
    merged.head should have length controller.getField()(0).size
  }

  it should "convert row to string" in {
    val controller = new TestController
    val tui = new Tui(controller)

    val row = Vector(0, 1, 2, 3, -1, 10, 11)
    val rowString = tui.rowToString(row)
    rowString shouldEqual "1 2 3 4 + # ? "
  }

  it should "update when controller events occur" in {
    val controller2 = new TestController
    val observer2 = new TestObserver
    controller2.addObserver(observer2)
    val tui2 = new Tui(controller2)

    // Perform an action that should trigger an update
    controller2.place(1)

    // Now, observer2.events should contain ControllerEvent.Update
    observer2.events should contain(ControllerEvent.Update)
  }

  it should "process invalid input" in {
    val controller = new TestController
    val tui = new Tui(controller)

    tui.processInput("invalid") shouldBe ()
  }
}
