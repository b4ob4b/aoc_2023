import utils.*
import utils.matrix.Matrix
import utils.matrix.Position

fun main() {
    Day10(IO.TYPE.SAMPLE).test(4, 1)
    Day10(IO.TYPE.SAMPLE2).test(70, 8)
    Day10().solve()
}

class Day10(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("Pipe Maze", inputType = inputType) {

    private val pipeSystem = input.toGrid().toMatrix()

    private fun String.findNeighbours() = when (this) {
        "S" -> if (isTest) listOf(Position(0, 1), Position(1, 0)) else listOf(Position(0, 1), Position(-1, 0))
        "-" -> listOf(Position(0, -1), Position(0, 1))
        "|" -> listOf(Position(1, 0), Position(-1, 0))
        "7" -> listOf(Position(0, -1), Position(1, 0))
        "J" -> listOf(Position(0, -1), Position(-1, 0))
        "F" -> listOf(Position(0, 1), Position(1, 0))
        "L" -> listOf(Position(0, 1), Position(-1, 0))
        else -> emptyList()
    }

    override fun part1(): Int {
        val s = pipeSystem.search("S")
            .first()

        val queue = ArrayDeque<Pair<Position, Int>>()
        queue.add(Pair(s, 0))
        val visited = mutableSetOf<Pair<Position, Int>>()
        while (queue.isNotEmpty()) {
            val (position, steps) = queue.removeFirst()
            if (visited.any { it.first == position }) continue
            visited.add(position to steps)

            val neighbours = pipeSystem[position].findNeighbours().map { it + position }
            neighbours.forEach { neighbour ->
                if (neighbour in pipeSystem) {
                    queue.add(neighbour to steps + 1)
                }
            }
        }

        return visited.maxOf { it.second }
    }

    override fun part2(): Int {
        val expandedPipesystem = pipeSystem.expand()

        val start = expandedPipesystem.search("S").first()

        val queuePath = ArrayDeque<Position>()
        queuePath.add(start)
        val visited = mutableSetOf<Position>()
        while (queuePath.isNotEmpty()) {
            val position = queuePath.removeFirst()
            if (position in visited) continue
            visited.add(position)

            val neighbours = expandedPipesystem[position].findNeighbours().map { it + position }
            neighbours.forEach { neighbour ->
                if (neighbour in expandedPipesystem) {
                    queuePath.add(neighbour)
                }
            }
        }

        val queueFlood = ArrayDeque<Position>()
        queueFlood.add(Position(0, 0))
        val flooded = mutableSetOf<Position>()
        while (queueFlood.isNotEmpty()) {
            val position = queueFlood.removeFirst()
            if (flooded.contains(position)) continue
            flooded.add(position)

            position.get8Neighbours().forEach { neighbour ->
                if (neighbour in expandedPipesystem && neighbour !in visited) {
                    queueFlood.add(neighbour)
                }
            }
        }

        val pointCandidates = sequence {
            expandedPipesystem.rowIndices.forEach { row ->
                expandedPipesystem.colIndices.forEach { col ->
                    if ((row - 1) % 3 == 0 && (col - 1) % 3 == 0) {
                        yield(Position(row, col))
                    }
                }
            }
        }.toList()

        return pointCandidates.count { it !in flooded && it !in visited }
    }

    private fun Matrix<String>.expand() = buildString {
        this@expand.matrix
            .forEachIndexed { row, rows ->
                var line1 = ""
                var line2 = ""
                var line3 = ""
                rows.forEachIndexed { col, cell ->
                    val newVal = when (cell) {
                        "." -> """
                        ...
                        ...
                        ...
                    """.trimIndent()

                        "F" -> """
                        ...
                        .F-
                        .|.
                    """.trimIndent()

                        "L" -> """
                        .|.
                        .L-
                        ...
                    """.trimIndent()

                        "7" -> """
                        ...
                        -7.
                        .|.
                    """.trimIndent()

                        "J" -> """
                        .|.
                        -J.
                        ...
                    """.trimIndent()


                        "-" -> """
                        ...
                        ---
                        ...
                    """.trimIndent()

                        "|" -> """
                        .|.
                        .|.
                        .|.
                    """.trimIndent()

                        else -> """
                        .|.
                        -${cell}-
                        .|.
                    """.trimIndent()
                    }
                    val g = newVal.splitLines()
                    line1 += g[0]
                    line2 += g[1]
                    line3 += g[2]
                }
                append(line1)
                appendLine()
                append(line2)
                appendLine()
                append(line3)
                appendLine()
            }
    }.toGrid()
        .toMatrix()
}