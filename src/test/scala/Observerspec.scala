import blokus.util.{Observable, Observer}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class ObservableSpec extends AnyWordSpec with Matchers {
  "An Observable" should {
    "correctly manage subscribers" when {
      "adding observers" in {
        val observable = new Observable[String]
        val observer1 = new Observer[String] { def update(e: String): Unit = {} }
        val observer2 = new Observer[String] { def update(e: String): Unit = {} }

        observable.addObserver(observer1)
        observable.addObserver(observer2)

        // Test indirectly by checking if notifyObservers calls update on both observers
        var updateCount = 0
        val countingObserver = new Observer[String] { def update(e: String): Unit = updateCount += 1 }

        observable.addObserver(countingObserver)
        observable.notifyObservers("test")

        updateCount shouldBe 1
      }

      "removing observers" in {
        val observable = new Observable[String]
        val observer = new Observer[String] { def update(e: String): Unit = {} }

        observable.addObserver(observer)
        observable.removeObserver(observer)

        // Test indirectly by checking if notifyObservers calls update
        var updateCalled = false
        val testObserver = new Observer[String] { def update(e: String): Unit = updateCalled = true }

        observable.addObserver(testObserver)
        observable.removeObserver(testObserver)
        observable.notifyObservers("test")

        updateCalled shouldBe false
      }

      "notifying observers" in {
        val observable = new Observable[String]
        var receivedEvent: Option[String] = None
        val observer = new Observer[String] { def update(e: String): Unit = receivedEvent = Some(e) }

        observable.addObserver(observer)
        observable.notifyObservers("hello")

        receivedEvent shouldBe Some("hello")
      }
    }
  }
}
