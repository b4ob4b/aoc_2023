package utils

/*
* x is the column
* y is the row
* y goes "up"
*
* */
data class Field<T>(val field: List<List<T>>) {

    constructor(x: Int, y: Int, element: () -> T) : this(List(x * y) { element.invoke() }.chunked(x))

    val numberOfRows = field.size
    val numberOfCols = field.first().size

    val rowIndices = 0 until numberOfRows
    val colIndices = 0 until numberOfCols

    operator fun get(position: Position) = field[position.y][position.x]

    fun search(element: T) = sequence {
        (0 until numberOfRows).flatMap { y ->
            (0 until numberOfCols).map { x ->
                if (field[y][x] == element) yield(Position(x, y))
            }
        }
    }


    fun insertAt(position: Position, element: T) = this
        .map { positionOfElement, cell ->
            if (position == positionOfElement) element else cell
        }

    fun insertAt(positionMap: Map<Position, T>) = this
        .map { position, cell ->
            if (positionMap.containsKey(position)) positionMap[position]!! else cell
        }

    fun <R> map(it: (position: Position, cell: T) -> R): Field<R> {
        return field
            .mapIndexed { y, xs ->
                xs.mapIndexed { x, cell ->
                    val position = Position(x, y)
                    it(position, cell)
                }
            }.toField()
    }

    fun highlight(highlight: (position: Position, cell: T) -> Boolean) {
        val highlightColor = "\u001b[" + 43 + "m"
        val defaultColor = "\u001b[" + 0 + "m"
        this
            .map { position, cell ->
                if (highlight(position, cell)) {
                    "$highlightColor$cell$defaultColor"
                } else {
                    cell.toString()
                }
            }
            .print()
    }


    override fun toString() = field
        .joinToString("\n") { row ->
            row.joinToString(" ")
        }
}
