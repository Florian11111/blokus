package de.htwg.se.blokus.models

import de.htwg.se.blokus.models.FieldImpl.* 

trait FieldInterface {
    val width: Int
    val height: Int
    def getFieldVector: Vector[Vector[Int]]
    
    // def isCorner(x: Int, y: Int): Boolean // ist private als muss nicht hier sein :) (Glaube ich)
    def cornerCheck(hoverBlock: HoverBlockInterface): Boolean
    def isInCorner(hoverBlock: HoverBlockInterface): Boolean
    def isGameRuleConfirm(hoverBlock: HoverBlockInterface): Boolean
    def isInsideField(hoverBlock: HoverBlockInterface): Boolean
    def placeBlock(hoverBlock: HoverBlockInterface, firstPlace: Boolean): FieldInterface
    def copy(): FieldInterface
}

object FieldInterface {
    def getInstance(width: Int, height: Int): FieldInterface = new Field(Vector.fill(height, width)(-1))
}
