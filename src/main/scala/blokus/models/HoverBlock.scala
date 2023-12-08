package blokus.models
import blokus.models.Block

class HoverBlock(playerAmount: Int, firstBlock: Int) {
    var currentX = 2
    var currentY = 2
    var currentBlockTyp = firstBlock
    var rotation = 0
    var mirrored = false
    var currentPlayer = 0

    def getX(): Int = currentX
    def getY(): Int = currentY
    def getRotation(): Int = rotation
    def getCurrentPlayer: Int = currentPlayer
    def getCurrentBlock: Int = currentBlockTyp
    def getBlock(): List[(Int, Int)] = {
        Block.createBlock(currentBlockTyp, rotation, mirrored).map { case (x, y) => (x + currentX, y + currentY) }
    }

    def setCurrentBlock(newBlock: Int): Int = {
        currentBlockTyp = newBlock
        currentBlockTyp
    }
    def changePlayer(): Int = {
        currentPlayer = (currentPlayer + 1) % playerAmount
        currentPlayer
    }

    def setPlayer(newPlayer: Int): Int = {
        val prevPlayer = currentPlayer
        currentPlayer = newPlayer
        prevPlayer
    }

    def getOutOfCorner(fieldHeight: Int, fieldWight: Int): Boolean = {
        if (currentX < 2) {
            currentX = 2
            true
        } else if ((currentX > fieldWight - 2)) {
            currentX = fieldWight - 2
            true
        } else if (currentY < 2) {
            currentY = 2
            true
        } else if ((currentY > fieldHeight - 2)) {
            currentY = fieldHeight - 2
            true
        } else {
            false
        }
    }

    def move(feld: Field, richtung: Int): Boolean = {
        if (canMove(feld, richtung)) {
            richtung match {
                case 0 => currentY += 1
                case 1 => currentX += 1
                case 2 => currentY -= 1
                case 3 => currentX -= 1
                case _ => // Ungültige Richtung, keine Aktion erforderlich
            }
            true
        } else {
            false
        }
    }

    def canMove(feld: Field, richtung: Int): Boolean = {
    richtung match {
        case 0 => 
            feld.isValidPosition(Block.createBlock(currentBlockTyp, rotation, mirrored), currentX, currentY + 1)
        case 1 => 
            feld.isValidPosition(Block.createBlock(currentBlockTyp, rotation, mirrored), currentX + 1, currentY)
        case 2 => 
            feld.isValidPosition(Block.createBlock(currentBlockTyp, rotation, mirrored), currentX, currentY - 1)
        case 3 => 
            feld.isValidPosition(Block.createBlock(currentBlockTyp, rotation, mirrored), currentX - 1, currentY)
        case _ => 
            false
        }
    }

    def canRotate(feld: Field): Boolean = {
        feld.isValidPosition(Block.createBlock(currentBlockTyp, (rotation + 1) % 4, mirrored), currentX, currentY)
    }

    // Dreht den aktuellen Block um 90 Grad im Uhrzeigersinn
    def rotate(feld: Field): Boolean = {
        if (canRotate(feld)) {
            rotation = (rotation + 1) % 4
            true
        } else {
            false
        }
    }

    def canMirror(feld: Field): Boolean = {
        feld.isValidPosition(Block.createBlock(currentBlockTyp, rotation, !mirrored), currentX, currentY)
    }

    // Spiegelt den aktuellen Block horizontal
    def mirror(feld: Field): Boolean = {
        if (canMirror(feld)) {
            mirrored = !mirrored
            true
        } else {
            false
        }
    }

    def canPlace(feld: Field): Boolean = {
        feld.isValidPlace(Block.createBlock(currentBlockTyp, rotation, mirrored), currentX, currentY, currentPlayer)
    }

    // Setzt den Block an der aktuellen Position
    def place(feld: Field, newBlockTyp: Int): Field = {
        val temp = feld.placeBlock(Block.createBlock(currentBlockTyp, rotation, mirrored), currentX, currentY, currentPlayer)
        currentX = 2
        currentY = 2
        rotation = 0
        mirrored = false
        currentBlockTyp = newBlockTyp
        temp
    }
}

object HoverBlock {
    private var instance: HoverBlock = _
    def getInstance(playerAmount: Int, firstBlock: Int): HoverBlock = {
        if (instance == null) {
        instance = new HoverBlock(playerAmount, firstBlock)
        }
        instance
    }
}