package de.htwg.se.blokus

import com.google.inject.{Guice, Injector}
import de.htwg.se.blokus.controller.GameController
import de.htwg.se.blokus.controller.controllerInvImpl.Controller
import de.htwg.se.blokus.models.fieldImpl.Field
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BlokusModuleSpec extends AnyWordSpec with Matchers {
  "BlokusModule" should {
    "bind GameController to an instance of Controller" in {
      val injector: Injector = Guice.createInjector(new BlokusModule)
      val gameController: GameController = injector.getInstance(classOf[GameController])

      gameController shouldBe a[Controller]
    }
  }
}
