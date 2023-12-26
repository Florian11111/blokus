package de.htwg.se.blokus.models.FieldImpl
import de.htwg.se.blokus.models.FieldInterface
import de.htwg.se.blokus.models.HoverBlockInterface
import de.htwg.se.blokus.models.Block


class Field(private val fieldVector: Vector[Vector[Int]]) extends FieldInterface {
    val width: Int = fieldVector.headOption.map(_.size).getOrElse(0)
    val height: Int = fieldVector.size

    def getFieldVector: Vector[Vector[Int]] = fieldVector

    def isCorner(x: Int, y: Int): Boolean = {
        (x == 0 && y == 0) || (x == width - 1 && y == 0) || (x == 0 && y == height - 1) || (x == width - 1 && y == height - 1)
    }

    private def isInBounds(x: Int, y: Int): Boolean = x >= 0 && x < width && y >= 0 && y < height

    def isInsideField(hoverBlock: HoverBlockInterface): Boolean = {
        val block = Block.createBlock(hoverBlock.getBlockType, hoverBlock.getRotation, hoverBlock.getMirrored)
        block.baseForm.forall { case (dx, dy) =>
            val newX = hoverBlock.getX + dx
            val newY = hoverBlock.getY + dy
            
            newY >= 0 && newY < height && newX >= 0 && newX < width
        }
    }

    def cornerCheck(hoverBlock: HoverBlockInterface): Boolean = {
        val block = Block.createBlock(hoverBlock.getBlockType, hoverBlock.getRotation, hoverBlock.getMirrored)
        block.corners.exists { ecke =>
            val newX = hoverBlock.getX + ecke._1
            val newY = hoverBlock.getY + ecke._2
            isGameRuleConfirm(hoverBlock)
        }
    }

    def isNoBlocksOnTop(hoverBlock: HoverBlockInterface): Boolean = {
        val block = Block.createBlock(hoverBlock.getBlockType, hoverBlock.getRotation, hoverBlock.getMirrored)
        block.baseForm.forall { case (dx, dy) =>
            val newX = hoverBlock.getX + dx
            val newY = hoverBlock.getY + dy
            isInBounds(newX, newY) && fieldVector(newY)(newX) == -1
        }
    }

    def isInCorner(hoverBlock: HoverBlockInterface): Boolean = {
        val block = Block.createBlock(hoverBlock.getBlockType, hoverBlock.getRotation, hoverBlock.getMirrored)
        isInsideField(hoverBlock) && 
        isNoBlocksOnTop(hoverBlock) &&
        block.baseForm.exists {case (dx, dy) => isCorner(hoverBlock.getX + dx, hoverBlock.getY + dy)}
    }

    def isGameRuleConfirm(hoverBlock: HoverBlockInterface): Boolean = {
        val block = Block.createBlock(hoverBlock.getBlockType, hoverBlock.getRotation, hoverBlock.getMirrored)
        if (isInsideField(hoverBlock) && isNoBlocksOnTop(hoverBlock)) {
            if (block.edges.forall { case (dx, dy) => !isInBounds(hoverBlock.getX + dx, hoverBlock.getY + dy) ||
                (isInBounds(hoverBlock.getX + dx, hoverBlock.getY + dy) && 
                fieldVector(hoverBlock.getY + dy)(hoverBlock.getX + dx) != hoverBlock.getPlayer)}) {
                block.corners.exists { case (dx, dy) => isInBounds(hoverBlock.getX + dx, hoverBlock.getY + dy) && 
                    fieldVector(hoverBlock.getY + dy)(hoverBlock.getX + dx) == hoverBlock.getPlayer}
            } else {
                false
            }
        } else {
            false
        }
    }

    def placeBlock(hoverBlock: HoverBlockInterface, firstPlace: Boolean): Field = {
        val block = Block.createBlock(hoverBlock.getBlockType, hoverBlock.getRotation, hoverBlock.getMirrored)
        if (isGameRuleConfirm(hoverBlock) || (firstPlace && isInCorner(hoverBlock))) {
            val updatedField = fieldVector.zipWithIndex.map { case (row, rowIndex) =>
                row.zipWithIndex.map { case (_, colIndex) =>
                if (block.baseForm.contains((colIndex - hoverBlock.getX, rowIndex - hoverBlock.getY)))
                    hoverBlock.getPlayer
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