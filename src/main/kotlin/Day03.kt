import utils.*
import utils.matrix.Position

fun main() {
    Day03(IO.TYPE.SAMPLE).test(4361, 467835)
    Day03().solve()
}

class Day03(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("", inputType = inputType) {

    val data = input.splitLines()

    override fun part1(): Any? {
        val m = data
            .map { it.split("") }.toMatrix()
//        [, ., 4, 2, 6, 9, 8, 5, 0, 7, 1, 3, &, +, -, #, @, $, *, /, %, =]
//        [, 4, 6, 7, ., 1, *, 3, 5, #, +, 8, 9, 2, $]

        val symbols = m.search2 { Regex("\\d|\\.").matches(m[it]).not() }
            .filter { m[it].isNotEmpty() }
            .toList()

        val numbersWithN = mutableSetOf<Position>()

        for (s in symbols) {
            val queue = ArrayDeque<Position>()
            queue.add(s)
            val seen = mutableSetOf<Position>()
            while (queue.isNotEmpty()) {
                val p = queue.removeFirst()
                if (p in seen) continue
                seen.add(p)
                p.get8Neighbours().forEach {
                    if (it.row in m.rowIndices && it.col in m.colIndices) {
                        if (Regex("\\d").matches(m[it])) {
                            numbersWithN.add(it)
                            queue.add(it)
                        }
                    }
                }
            }

        }

        val mm = mutableListOf<String>()

        for (r in m.rowIndices) {
            var n = ""
            for (c in m.colIndices) {
                val p = Position(r, c)
                if (p in numbersWithN) {
                    n = "$n${m[p]}"
                } else {
                    mm.add(n)
                    n = ""

                }

            }
        }
        return mm
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
            .sum()

//        332970
        return "not yet implement"
    }

    override fun part2(): Any? {
        val m = data
            .map { it.split("") }.toMatrix()
//        [, ., 4, 2, 6, 9, 8, 5, 0, 7, 1, 3, &, +, -, #, @, $, *, /, %, =]
//        [, 4, 6, 7, ., 1, *, 3, 5, #, +, 8, 9, 2, $]

        val symbols = m.search2 { Regex("""\*""").matches(m[it]) }
            .filter { m[it].isNotEmpty() }
            .toList()

        val numbersWithN = mutableSetOf<Position>()

        for (s in symbols) {
            val queue = ArrayDeque<Position>()
            queue.add(s)
            val seen = mutableSetOf<Position>()
            while (queue.isNotEmpty()) {
                val p = queue.removeFirst()
                if (p in seen) continue
                seen.add(p)
                p.get8Neighbours().forEach {
                    if (it.row in m.rowIndices && it.col in m.colIndices) {
                        if (Regex("\\d").matches(m[it])) {
                            numbersWithN.add(it)
                            queue.add(it)
                        }
                    }
                }
            }
        }
        "h".print()
        numbersWithN.first().print()

        val np = mutableSetOf<MutableSet<Position>>()

        for (r in m.rowIndices) {
            var nn = mutableSetOf<Position>()
            for (c in m.colIndices) {
                val p = Position(r, c)
                if (p in numbersWithN) {
                    nn.add(p)
                } else {
                    if (nn.isNotEmpty()) {
                        nn.print()
                        np.add(nn)
                        np.print()
                        nn = mutableSetOf<Position>()
                    }
                }

            }
        }

        np.toList().print()

        val ff = mutableSetOf<MutableSet<MutableSet<Position>>>()

        symbols.first().print()
        symbols.forEach { ast ->
            var count = 0
            val found = mutableSetOf<MutableSet<Position>>()
            np.distinct().forEach {
                val i = ast.get8Neighbours().toSet().intersect(it).isNotEmpty()
                if (i) {
                    count = count + 1
                    found.add(it)
                }
            }

            if (count == 2) {
                ff.add(found)
            }
        }
        ff.toList().first().print()
        return ff.map {
            val numbers = mutableListOf<String>()
            for (r in m.rowIndices) {
                var n = ""
                for (c in m.colIndices) {
                    val p = Position(r, c)
                    if (p in it.flatten()) {
                        n = "$n${m[p]}"
                    } else {
                        numbers.add(n)
                        n = ""
                    }

                }
            }
            numbers
        }
            .filter { it.isNotEmpty() }
            .map {
                it.filter { it.isNotEmpty() }
//                .print()
                    .map { it.toInt() }.product()
            }
            .sum()



        return 1

//        332970
        return "not yet implement"
    }
}