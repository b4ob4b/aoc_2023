import utils.*
import utils.matrix.Position
import utils.navigation.Direction4

fun main() {
//    Day10(IO.TYPE.SAMPLE).test(4, 2)
    Day10(IO.TYPE.SAMPLE2).test(70, part2 = 8)
    Day10().solve()
}

class Day10(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("", inputType = inputType) {

    private val matrix = input.toGrid().toMatrix()
    val path = mutableListOf<Position>()

    fun String.n() = when (this) {
        "S" -> if (isTest) listOf(Position(0, 1), Position(1, 0)) else listOf(Position(0, 1), Position(-1, 0))
        "-" -> listOf(Position(0, -1), Position(0, 1))
        "|" -> listOf(Position(1, 0), Position(-1, 0))
        "7" -> listOf(Position(0, -1), Position(1, 0))
        "J" -> listOf(Position(0, -1), Position(-1, 0))
        "F" -> listOf(Position(0, 1), Position(1, 0))
        "L" -> listOf(Position(0, 1), Position(-1, 0))
        else -> emptyList()
    }

    override fun part1(): Any? {
        val s = matrix.search("S")
            .first()

        val queue = ArrayDeque<Pair<Position, Int>>()
        queue.add(Pair(s, 0))
        val visited = mutableSetOf<Pair<Position, Int>>()
        while (queue.isNotEmpty()) {
            val (position, steps) = queue.removeFirst()
            if (visited.any { it.first == position }) continue
            visited.add(position to steps)

            val neighbours = matrix[position].n().map { it + position }
            neighbours.forEach { neighbour ->
                if (neighbour in matrix) {
                    queue.add(neighbour to steps + 1)
                }
            }
        }


        path.addAll(visited.map { it.first }.toSet())

        return visited.maxOf { it.second }


        return "not yet implement"
    }

    override fun part2(): Any? {
        val nm = buildString {
            matrix.matrix
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

        val pp = sequence {
            nm.rowIndices.forEach { row ->
                nm.colIndices.forEach { col ->
                    if ((row - 1) % 3 == 0 && (col - 1) % 3 == 0) {
                        yield(Position(row, col))
                    }
                }
            }
        }.toList()

//        pp.print()
//
//        return 1


        val s = nm.search("S")
            .first()

        val queue0 = ArrayDeque<Pair<Position, Int>>()
        queue0.add(Pair(s, 0))
        val visited = mutableSetOf<Pair<Position, Int>>()
        while (queue0.isNotEmpty()) {
            val (position, steps) = queue0.removeFirst()
            if (visited.any { it.first == position }) continue
            visited.add(position to steps)

            val neighbours = nm[position].n().map { it + position }
            neighbours.forEach { neighbour ->
                if (neighbour in nm) {
                    queue0.add(neighbour to steps + 1)
                }
            }
        }

//        nm.highlight { position, cell ->
//            position in visited.map { it.first }
//        }
//        return 1

        val queue = ArrayDeque<Position>()
        queue.add(Position(0, 0))
        queue.add(Position(0, nm.numberOfCols - 1))
        queue.add(Position(nm.numberOfRows - 1, 0))
        queue.add(Position(nm.numberOfRows - 1, nm.numberOfCols - 1))
        val flodded = mutableSetOf<Position>()
        while (queue.isNotEmpty()) {
            val position = queue.removeFirst()
            if (flodded.contains(position)) continue
            flodded.add(position)

            position.get8Neighbours().forEach { neighbour ->
                if (neighbour in nm && neighbour !in visited.map { it.first }) {
                    queue.add(neighbour)
                }
            }
        }
//        nm.highlight { position, cell ->
//            position in flodded
//        }

        return pp.count { it !in flodded && it !in visited.map { it.first } }
        return 1

        val remaining = matrix.map { position, _ ->
            if (position !in flodded && position !in path) position else null
        }.matrix.flatten().mapNotNull { it }

        matrix.highlight { position, _ ->
            position in remaining
        }

        remaining.size.print()
        val mm = matrix.let { it.insertAt(it.search("S").single(), if (isTest) "F" else "L") }

        return remaining.map { position ->
            Direction4.entries.any { dir ->

                var next = position
                val pipes = mutableListOf<String>()
                do {
                    next = next.moveTo(dir)
                    if (next in path) {
                        pipes.add(mm[next])
                    }
                } while (next in matrix && next !in flodded)
                when (dir) {
                    Direction4.North -> pipes.calculateVertical()
                    Direction4.South -> pipes.calculateVertical()
                    Direction4.East -> pipes.calculateHorizontal()
                    Direction4.West -> pipes.calculateHorizontal()
                }
            }

        }
            .count { it == true }


        return matrix.numberOfCols * matrix.numberOfRows - flodded.size - path.size
//        622
//        616
//        362
//        38
//        546
        return "not yet implement"
    }

    private fun List<String>.calculateVertical(): Boolean {
        val m = this.toMutableList()
        val countLines = m.count { it == "-" }
        m.removeAll { it == "-" }
        m.removeAll { it == "." }
        m.removeAll { it == "|" }
//        m.removeAll { it == "S" }

        val pairs = mapOf(
            setOf("F", "J") to 1,
            setOf("L", "7") to 1,
            setOf("F", "L") to 0,
            setOf("J", "7") to 0
        )
        val count = m.chunked(2)
            .mapNotNull {
                pairs[setOf(it[0], it[1])]
            }.sum() + countLines
        return count % 2 == 1
    }

    private fun List<String>.calculateHorizontal(): Boolean {
        val m = this.toMutableList()
        val countLines = m.count { it == "|" }
        m.removeAll { it == "|" }
        m.removeAll { it == "-" }
        m.removeAll { it == "." }

        val pairs = mapOf(
            setOf("F", "J") to 0,
            setOf("L", "7") to 0,
            setOf("F", "L") to 1,
            setOf("J", "7") to 1
        )
        val count = m.chunked(2).mapNotNull {
            pairs[setOf(it[0], it[1])]
        }.sum() + countLines
        return count % 2 == 1
    }
}