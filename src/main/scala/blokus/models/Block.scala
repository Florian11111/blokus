package blokus.models

case class Block(baseForm: List[(Int, Int)])

object Block {
    val block1 = Block(List((0, 0)))
    val block2 = Block(List((0, 0), (1, 0)))
    val block3 = Block(List((-1, 0), (0, 0), (1, 0)))
    val block4 = Block(List((0, 0), (1, 0), (0, 1)))
    val block5 = Block(List((-1, 0), (0, 0), (1, 0), (2, 0)))
    val block6 = Block(List((-1, 0), (-1, 1), (0, 0), (1, 0)))
    val block7 = Block(List((-1, 0), (0, 0), (1, 0), (0, 1)))
    val block8 = Block(List((0, 0), (1, 0), (0, 1), (1, 1)))
    val block9 = Block(List((0, 0), (-1, 0), (0, 1), (1, 1)))
    val block10 = Block(List((-2, 0), (-1, 0), (0, 0), (1, 0), (2, 0)))
    val block11 = Block(List((-1, 1), (-1, 0), (0, 0), (1, 0), (1, 1)))
    val block12 = Block(List((-1, 0), (0, 0), (1, 0), (0, 1), (1, 1)))
    val block13 = Block(List((-1, 0), (0, 0), (1, 0), (2, 0), (0, 1)))
    val block14 = Block(List((-2, 0), (-1, 0), (0, 0), (0, 1), (1, 1)))
    val block15 = Block(List((-2, 0), (-1, 0), (0, 0), (1, 0), (1, 1)))
    val block16 = Block(List((0, 0), (0, -1), (1, 0), (0, 1), (-1, 0)))
    val block17 = Block(List((-1, -1), (0, -1), (0, 0), (0, 1), (1, 1)))
    val block18 = Block(List((-1, -1), (0, -1), (0, 0), (1, 0), (1, 1)))
    val block21 = Block(List((-1, -1), (0, -1), (1, -1), (1, 0), (1, 1)))
    val block19 = Block(List((-1, -1), (0, -1), (1, -1), (0, 0), (0, 1)))
    val block20 = Block(List((-1, -1), (0, -1), (0, 0), (1, 0), (0, 1)))


    def createBlock(blockType: Block, rotation: Int, mirrored: Boolean): List[(Int, Int)] = {
        var block = blockType.baseForm

        if (mirrored) {
            block = block.map { case (x, y) => (x, -y) }
        }
        for (_ <- 0 until rotation) {
            block = block.map { case (x, y) => (-y, x) }
        }
        block
    }
}


def main(args: Array[String]): Unit = {
    val blockType = Block.block6 // Wähle den gewünschten Blocktyp
    val rotation = 1
    val mirrored = true

    val rotatedBlock = Block.createBlock(blockType, rotation, mirrored)

    println(rotatedBlock)
}