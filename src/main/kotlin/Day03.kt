import utils.*
import utils.matrix.Matrix
import utils.matrix.Position

fun main() {
    Day03(IO.TYPE.SAMPLE).test(4361, 467835)
    Day03().solve()
}

class Day03(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("Gear Ratios", inputType = inputType) {

    private val engine = input.splitLines()
        .map { it.split("") }
        .toMatrix()

    private val matchesPartAt = fun(position: Position) = Regex("\\d|\\.|^$").matches(engine[position]).not()
    private val matchesGearAt = fun(position: Position) = Regex("\\*").matches(engine[position])
    private val matchesNumberAt = fun(position: Position) = Regex("\\d").matches(engine[position])

    override fun part1(): Int {

        val numbers = mutableListOf<Int>()
        for (row in engine.rowIndices) {
            var number = ""
            var hasSymbol = false
            for (col in engine.colIndices) {
                val p = Position(row, col)
                if (matchesNumberAt(p)) {
                    hasSymbol = hasSymbol || p.get8Neighbours().filter { it in engine }.any { matchesPartAt(it) }
                    number = "$number${engine[p]}"
                } else {
                    if (hasSymbol && number.isNotEmpty()) {
                        numbers.add(number.toInt())
                    }
                    number = ""
                    hasSymbol = false
                }
            }
        }
        return numbers.sum()
    }

    override fun part2(): Any? {
        val gearPositions = engine
            .search { position -> matchesGearAt(position) }
            .filter { engine[it].isNotEmpty() }
            .toList()

        val gears = gearPositions.map { it to mutableSetOf<Position>() }.toMap()

        for (gearPosition in gearPositions) {
            val queue = ArrayDeque<Position>()
            queue.add(gearPosition)
            val seen = mutableSetOf<Position>()
            while (queue.isNotEmpty()) {
                val position = queue.removeFirst()
                if (position in seen) continue
                seen.add(position)
                position.get8Neighbours().forEach { neighbour ->
                    if (neighbour in engine) {
                        if (matchesNumberAt(neighbour)) {
                            gears[gearPosition]?.add(neighbour)
                            queue.add(neighbour)
                        }
                    }
                }
            }
        }


        return gears
            .map { engine.findNumbers(it.value) }
            .filter { it.size == 2 }
            .sumOf { it.product() }
    }

    private fun Matrix<String>.findNumbers(positions: Set<Position>): List<Int> {
        val numbers = mutableListOf<Int>()
        for (r in rowIndices) {
            var number = ""
            for (c in colIndices) {
                val position = Position(r, c)
                number = if (position in positions) {
                    "$number${this[position]}"
                } else {
                    if (number.isNotEmpty()) {
                        numbers.add(number.toInt())
                    }
                    ""
                }
            }
        }
        return numbers.toList()
    }
}