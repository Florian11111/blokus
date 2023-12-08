package blokus.models

class Field(private val fieldVector: Vector[Vector[Int]]) {
    val width: Int = fieldVector.headOption.map(_.size).getOrElse(0)
    val height: Int = fieldVector.size

    def getFieldVector: Vector[Vector[Int]] = fieldVector

    def isValidPosition(block: List[(Int, Int)], x: Int, y: Int): Boolean = {
    block.forall { case (dx, dy) =>
        val newY = y + dy
        val newX = x + dx
        newY >= 0 && newY < height && newX >= 0 && newX < width
    }
    }

    def isGameOver(block: List[(Int, Int)], x: Int, y: Int, currentPlayer: Int): Boolean = {
        if (!isValidPosition(block, x, y) || !block.forall { case (dx, dy) => fieldVector(y + dy)(x + dx) == -1 }) {
            false
        } else {
            true
        }
    }
    
    def isValidPlace(block: List[(Int, Int)], x: Int, y: Int, currentPlayer: Int): Boolean = {
        if (!isValidPosition(block, x, y) || !block.forall { case (dx, dy) => fieldVector(y + dy)(x + dx) == -1 }) {
            false
        } else {
            // isLogicPlace
            true
        }
    }

    // geht durch den block und speichert alle ecken in einer liste. dann wird der block nochmal durchgegannen
    // und alle direkten nachbarn gecheckt. => muss fals sein wenn es einen nachbarn vom selben block gibt der nicht
    // teil des blocks ist und löscht alle direkten nachbarn aus der liste. Dann wird die ecken liste überprüft ob es 
    // mindestens einen match gibt
    /*
    def getAllCorners(blocks: List[List[(Int, Int)]]): List[(Int, Int)] = {
        blocks.flatMap { block =>
        block.flatMap { case (x, y) =>
            List(
            (x + 1, y + 1),
            (x + 1, y - 1),
            (x - 1, y + 1),
            (x - 1, y - 1)
            )
        }
        }.distinct
    }*/


    def placeBlock(block: List[(Int, Int)], x: Int, y: Int, currentPlayer: Int): Field = {
        if (isValidPlace(block, x, y, currentPlayer)) {
        val updatedField = fieldVector.zipWithIndex.map { case (row, rowIndex) =>
            row.zipWithIndex.map { case (_, colIndex) =>
            if (block.contains((colIndex - x, rowIndex - y)))
                currentPlayer
            else
                fieldVector(rowIndex)(colIndex)
            }
        }
        new Field(updatedField)
        } else {
        throw new IllegalArgumentException("Invalid position for block placement.")
        }
    }
    def copy(): Field = {
        val copiedVector = fieldVector.map(_.toVector).toVector
        new Field(copiedVector)
    }
}

object Field {
    private var instance: Option[Field] = None

    def getInstance(width: Int, height: Int): Field = {
        instance.getOrElse {
        val initialFieldVector = Vector.fill(height, width)(-1)
        val fieldInstance = new Field(initialFieldVector)
        instance = Some(fieldInstance)
        fieldInstance
        }
    }

    def apply(width: Int, height: Int): Field = new Field(Vector.fill(height, width)(-1))
}