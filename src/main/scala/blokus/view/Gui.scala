package blokus.view

import blokus.controller.Controller
import blokus.controller.ControllerEvent
import blokus.util.Observer
import scalafx.Includes._
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.{GridPane, HBox, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.application.Platform


class Gui(controller: Controller, windowsWidth: Int, windowsHeight: Int) extends JFXApp3 with Observer[ControllerEvent] {
    controller.addObserver(this)

    private var boardPane: GridPane = _
    private var currentPlayerLabel: Label = _
    private var blocksLabel: Label = _
    private var numberTextField: TextField = _

    override def start(): Unit = {
        stage = new JFXApp3.PrimaryStage {
            title.value = "Blokus Game"
            width = windowsWidth + 20
            height = windowsHeight + 20
            scene = createScene()
        }
        updateBoard()
        updateLabels()
    }

    def createScene(): Scene = {
        new Scene {
            fill = Color.web("#333333",1)
            content = new VBox {
                spacing = 10
                alignment = scalafx.geometry.Pos.Center

                // Initialisiere currentPlayerLabel hier
                currentPlayerLabel = new Label("Current Player: ")
                children.add(currentPlayerLabel)

                blocksLabel = new Label(controller.getBlocks().toString)
                children.add(blocksLabel)

                boardPane = new GridPane {}

                children.add(boardPane)
                
                // Erstelle eine HBox für die Buttons
                val buttonBox1 = new HBox {
                    spacing = 10
                    alignment = scalafx.geometry.Pos.Center
                }

                // Füge 6 Buttons hinzu und weise ihnen eine Funktion zu
                val button1 = new Button(s"Hoch")
                styleButton(button1)
                button1.onAction = _ => handleButtonAction(0)
                buttonBox1.children.add(button1)

                val button2 = new Button(s"Runter")
                styleButton(button2)
                button2.onAction = _ => handleButtonAction(1)
                buttonBox1.children.add(button2)

                val button3 = new Button(s"Links")
                styleButton(button3)
                button3.onAction = _ => handleButtonAction(2)
                buttonBox1.children.add(button3)

                val button4 = new Button(s"Rechts")
                styleButton(button4)
                button4.onAction = _ => handleButtonAction(3)
                buttonBox1.children.add(button4)

                val button5 = new Button(s"Spiegeln")
                styleButton(button5)
                button5.onAction = _ => handleButtonAction(4)
                buttonBox1.children.add(button5)

                val button6 = new Button(s"Drehen")
                styleButton(button6)
                button6.onAction = _ => handleButtonAction(5)
                buttonBox1.children.add(button6)

                children.add(buttonBox1)

                // Erstelle eine HBox für die Buttons
                val buttonBox2 = new HBox {
                    spacing = 10
                    alignment = scalafx.geometry.Pos.Center
                }

                val button7 = new Button(s"Setzten")
                styleButton(button7)
                button7.onAction = _ => handleButtonAction(6)
                buttonBox2.children.add(button7)

                val button8 = new Button(s"Undo")
                styleButton(button8)
                button8.onAction = _ => handleButtonAction(7)
                buttonBox2.children.add(button8)

                val button9 = new Button(s"BlockWechseln")
                styleButton(button9)
                button9.onAction = _ => handleButtonAction(8)
                buttonBox2.children.add(button9)

                numberTextField = new TextField {
                    promptText = "Block wechsel"
                }
                buttonBox2.children.add(numberTextField)

                children.add(buttonBox2)
            }
        }
    }

    def styleButton(button: Button): Unit = {
        button.style = "-fx-font-size: 10pt;" +
        "-fx-background-color: #a6a6a6, linear-gradient(#aaaaaa, #808080)," +
        "linear-gradient(#aaaaaa 0%, #808080 50%, #666666 51%, #757575 100%);" +
        "-fx-background-radius: 6, 5;" +
        "-fx-background-insets: 0, 1;" +
        "-fx-text-fill: black;" +
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 1);"
    }


    def createButtonBox(buttonLabels: Seq[String]): HBox = {
        new HBox {
        spacing = 10
        alignment = scalafx.geometry.Pos.Center

        for ((label, index) <- buttonLabels.zipWithIndex) {
            val button = new Button(label)
            button.onAction = _ => handleButtonAction(index)
            button.styleClass = Seq("rounded-button")
            children.add(button)
        }
        }
    }

    // Beispiel-Funktion, die von den Buttons aufgerufen wird
    def handleButtonAction(buttonNumber: Int): Unit = {
        buttonNumber match {
            case 0 => controller.move(2)
            case 1 => controller.move(0)
            case 2 => controller.move(3)
            case 3 => controller.move(1)
            case 4 => controller.mirror()
            case 5 => controller.rotate()
            case 6 => {
                if (controller.canPlace()) {
                    controller.place(5)
                    controller.nextPlayer()
                } else {
                    println("Kann nicht an dieser Stelle Platziert werden!")
                }
            }
            case 7 => controller.undo()
            case 8 => {
                val inputText = numberTextField.text.value
                if (inputText.matches("""\d+""")) {
                    val blockIndex = inputText.toInt
                    if (blockIndex >= 0 && blockIndex <= 19) {
                        val blocks = controller.getBlocks()
                        if (blocks(blockIndex) > 0) {
                            controller.changeCurrentBlock(blockIndex)
                        } else {
                            print("Block nicht mehr verfuegbar!")
                        }
                    } else {
                        print("Wahle Block Zwischen 0 und 19!")
                    }
                } else {
                    print("Bitte gebe eine Zahl an!")
                }
            }
            case _ =>
        }
    }


    override def update(event: ControllerEvent): Unit = {
        Platform.runLater(() => {
            updateBoard()
            updateLabels()
        })
    }

    def mergeFieldAndBlock(): Vector[Vector[Int]] = {
        val field = controller.getField()
        val block = controller.getBlock()

        val merged = field.zipWithIndex.map { case (row, rowIndex) =>
            row.zipWithIndex.map { case (fieldValue, columnIndex) =>
                block.find { case (x, y) =>
                    x == columnIndex && y == rowIndex
                }.map(_ => if (fieldValue != -1) 11 else 20 + controller.getCurrentPlayer()).getOrElse(fieldValue)
            }
        }
        merged
    }

    def updateBoard(): Unit = {
        val mergedFieldAndBlock = mergeFieldAndBlock()
        boardPane.children.clear()
        for {
            (row, rowIndex) <- mergedFieldAndBlock.zipWithIndex
            (value, columnIndex) <- row.zipWithIndex
        } {
            val rectangle = new Rectangle {
                width = (windowsWidth / controller.getWidth()).toInt
                height = (windowsWidth / controller.getWidth()).toInt
                fill = getFillColor(value)
            }
            boardPane.add(rectangle, columnIndex, rowIndex)
        }
    }

    def getFillColor(value: Int): Color = {
        value match {
            case -1 => Color.White
            case 0 => Color.Red
            case 1 => Color.Green
            case 2 => Color.Blue
            case 3 => Color.Yellow
            case 10 => Color.Gray
            case 11 => Color.LightGray
            case 20 => Color.web("#8b0000",1)
            case 21 => Color.web("#006400",1)
            case 22 => Color.web("#000088",1)
            case 23 => Color.web("#9B870C",1)
            case _ => Color.Black
        }
    }

    def updateLabels(): Unit = {
        val currentPlayerLabelText = s"Current Player: ${controller.getCurrentPlayer() + 1}"
        val blocksLabelText = s"Blocks: ${controller.getBlocks()}"

        currentPlayerLabel.text = currentPlayerLabelText
        blocksLabel.text = blocksLabelText

        // Ändere die Textfarbe und den Hintergrund der Labels
        currentPlayerLabel.style = "-fx-text-fill: white;" // oder eine andere gewünschte Farbe
        blocksLabel.style = "-fx-text-fill: white;" // oder eine andere gewünschte Farbe
    }
}
