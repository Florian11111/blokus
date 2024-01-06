package de.htwg.se.blokus.util

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import de.htwg.se.blokus.util.{Observable, Observer}

class ObservableSpec extends AnyWordSpec with Matchers {
  case class TestEvent(value: Int)

  class TestObserver extends Observer[TestEvent] {
    var receivedEvents: List[TestEvent] = List()

    override def update(event: TestEvent): Unit = {
      receivedEvents = event :: receivedEvents
    }
  }

  class TestObservable extends Observable[TestEvent]

  "Observable" should {
    "add and notify observers" in {
      val observable = new TestObservable
      val observer1 = new TestObserver
      val observer2 = new TestObserver

      observable.addObserver(observer1)
      observable.addObserver(observer2)

      val event = TestEvent(42)
      observable.notifyObservers(event)

      observer1.receivedEvents should contain only event
      observer2.receivedEvents should contain only event
    }

    "remove observers" in {
      val observable = new TestObservable
      val observer1 = new TestObserver
      val observer2 = new TestObserver

      observable.addObserver(observer1)
      observable.addObserver(observer2)

      observable.removeObserver(observer1)

      val event = TestEvent(42)
      observable.notifyObservers(event)

      observer1.receivedEvents shouldBe empty
      observer2.receivedEvents should contain only event
    }

    "not notify removed observers" in {
      val observable = new TestObservable
      val observer1 = new TestObserver
      val observer2 = new TestObserver

      observable.addObserver(observer1)
      observable.addObserver(observer2)

      observable.removeObserver(observer1)

      val event = TestEvent(42)
      observable.notifyObservers(event)

      observer1.receivedEvents shouldBe empty
    }
  }
}
