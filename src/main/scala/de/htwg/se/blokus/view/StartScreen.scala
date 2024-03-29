package de.htwg.se.blokus.view

import scalafx.collections.ObservableBuffer
import scalafx.geometry.Pos
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, TextField, CheckBox}
import scalafx.scene.layout.{BorderPane, HBox, VBox}
import scalafx.scene.text.Font
import scalafx.scene.paint.Color
import scalafx.scene.layout.StackPane
import scalafx.scene.layout.Pane
import de.htwg.se.blokus.controller.GameController
import javafx.stage.Stage
import scalafx.application.Platform

class StartScene(gui: Gui, controller: GameController) {
    private val inputFields = new ObservableBuffer[Pane]()

    private val playerLabel = new Label("Players: 2") {
        font = Font("Arial", 16)
        style = "-fx-text-fill: white;"
    }
    private val errorLabel = new Label("") {
        font = Font("Arial", 12)
        style = "-fx-text-fill: red;"
    }

    // Erstellen Sie zwei Eingabefelder beim Start
    inputFields += new Pane {
        children = Seq(new scalafx.scene.control.TextField() {
            style = "-fx-text-fill: white; -fx-background-color: black; -fx-alignment: center; -fx-padding: 10; -fx-border-color: #4CAF50; -fx-border-radius: 5;"
            promptText = "Name"
        })
        margin = scalafx.geometry.Insets(5) // Fügt einen Rand von 10 Pixeln hinzu
    }
    inputFields += new Pane {
        children = Seq(new scalafx.scene.control.TextField() {
            style = "-fx-text-fill: white; -fx-background-color: black; -fx-alignment: center; -fx-padding: 10; -fx-border-color: #4CAF50; -fx-border-radius: 5;"
            promptText = "Name"
        })
        margin = scalafx.geometry.Insets(5) // Fügt einen Rand von 10 Pixeln hinzu
    }

    private val performanceCheckBox = new CheckBox {
        text = "High Performance Mode"
        style = "-fx-text-fill: white;"
    }
    private val infoCheckBox = new CheckBox {
        text = "Info"
        style = "-fx-text-fill: white;"
        selected = true
    }
    private val sizeInputFields = Seq(
        new TextField() {
            style = "-fx-text-fill: white; -fx-background-color: black; -fx-alignment: center; -fx-border-color: #606060; -fx-border-radius: 5;"
            promptText = "X"
            text = "20"
            prefColumnCount = 5  
        },
        new TextField() {
            style = "-fx-text-fill: white; -fx-background-color: black; -fx-alignment: center; -fx-border-color: #606060; -fx-border-radius: 5;"
            promptText = "Y"
            text = "20"
            prefColumnCount = 5  
        }
    )
    private val vbox = new VBox {
        alignment = Pos.CenterRight
    }
    private val buttons = new HBox {
        alignment = Pos.CenterLeft
    }

    def createScene(): Scene = {
        val rootPane = new BorderPane {
            right = new VBox {
                spacing = 6
                alignment = Pos.CenterLeft
                children = sizeInputFields ++ Seq(performanceCheckBox) ++ Seq(infoCheckBox)
                margin = scalafx.geometry.Insets(0, 20, 0, 0)
            }
            style = "-fx-background-color: #191819;"
            top = new VBox {
                alignment = Pos.Center
                children = Seq(
                    new Label("Blokus") {
                        margin = scalafx.geometry.Insets(25, 0, 10, 0)
                        font = Font("Arial", 40)
                        style = "-fx-text-fill: white;"
                    },
                    playerLabel,
                    errorLabel
                )
            }
            center = vbox
            vbox.alignment = Pos.Center
            buttons.children = Seq(
                new Button("+") {
                    margin = scalafx.geometry.Insets(0, 10, 0, 25)
                    style = "-fx-border-color: #b9b8b9; -fx-border-radius: 5; -fx-text-fill: white; -fx-background-color: #292829;"
                    onAction = _ => {
                        if (inputFields.size < 4) {
                            val newTextField = new scalafx.scene.control.TextField() {
                                style = "-fx-text-fill: white; -fx-background-color: black; -fx-alignment: center; -fx-padding: 10; -fx-border-color: #4CAF50; -fx-border-radius: 5;"
                                promptText = "Name"
                            }

                            val newField = new Pane {
                                children = Seq(newTextField)
                                margin = scalafx.geometry.Insets(5)
                            }

                            inputFields += newField
                            vbox.children = inputFields ++ Seq(buttons)

                            Platform.runLater(() => newTextField.requestFocus())
                        }

                        playerLabel.text = s"Player: ${inputFields.size}"
                        errorLabel.text = ""
                    }
                    },
                new Button("-") {
                    style = "-fx-border-color: #b9b8b9; -fx-border-radius: 5; -fx-text-fill: white; -fx-background-color: #292829;;"
                    onAction = _ => {
                        if (inputFields.size > 1) {
                            val lastField = inputFields.last
                            inputFields -= lastField
                            vbox.children = inputFields ++ Seq(buttons)
                            playerLabel.text = s"Players: ${inputFields.size}"
                            errorLabel.text = ""
                        }
                    }
                    margin = scalafx.geometry.Insets(0, 25, 0, 0)
                }
            )
            Platform.runLater(() => inputFields.head.children(0).requestFocus())
            vbox.children = inputFields ++ Seq(buttons)
            bottom = new StackPane {
                alignment = Pos.Center
                children = Seq(
                    new Button("Start Game") {
                        style = "-fx-text-fill: white; -fx-background-color: black; -fx-alignment: center; -fx-padding: 10; -fx-border-color: #4CAF50; -fx-border-radius: 5;"
                        onAction = _ => {
                            val names = inputFields.map(_.children(0).asInstanceOf[javafx.scene.control.TextField].getText)
                            if (names.exists(_.isEmpty)) {
                                errorLabel.text = "Error: One or more fields are empty."
                            } else if (names.distinct.size != names.size) {
                                errorLabel.text = "Error: Duplicate names are not allowed."
                            } else {
                                val x = sizeInputFields(0).getText
                                val y = sizeInputFields(1).getText
                                if (!x.matches("[0-9]+") || !y.matches("[0-9]+")) {
                                    errorLabel.text = "Error: X and Y must be numbers."
                                    
                                } else {
                                    controller.start(names.size, x.toInt, y.toInt)
                                    gui.switchToGameScene(names.toList, performanceCheckBox.isSelected, infoCheckBox.isSelected)
                                }
                                
                            }
                        }
                    }
                )
                margin = scalafx.geometry.Insets(0, 0, 20, 0)
            }
        }
        new Scene {
            fill = Color.Black 
            root = rootPane
        }
    }
}
