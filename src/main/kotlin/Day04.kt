import utils.Day
import utils.IO
import utils.print
import utils.splitLines

fun main() {
    Day04(IO.TYPE.SAMPLE).test(13, 30)
    Day04().solve()
}

class Day04(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("", inputType = inputType) {

    val data = input.splitLines()

    val i = listOf(0) + generateSequence(1) { it * 2 }.take(10).toList()

    override fun part1(): Any? {
        return data
            .map { it.split(": |\\|".toRegex()).drop(1) }
            .map { it.map { it.split(" ").filter { it.isNotEmpty() }.map { it.toInt() } } }
            .map { it[0] to it[1] }
            .map { it.first.toSet() intersect it.second.toSet() }
            .map { it.size }
            .map { i[it] }
            .sum()
        return "not yet implement"
    }

    override fun part2(): Any? {
        val wins = data
            .map { it.split(": |\\|".toRegex()).drop(1) }
            .map { it.map { it.split(" ").filter { it.isNotEmpty() }.map { it.toInt() } } }
            .map { it[0] to it[1] }
            .map { it.first.toSet() intersect it.second.toSet() }
            .map { it.size }

            .print()
        val cards = mutableMapOf<Int, MutableList<Int>>()
        for (i in 1..data.size) {
            cards.put(i, mutableListOf())
        }
        for (i in 1..data.size) {
            cards[i]?.add(1)
            val additional2 = if (cards[i] == null) 0 else cards[i]?.sum()
            val additional = if (additional2 == null) 0 else additional2
            for (ii in 1..(wins[i - 1])) {
                cards[ii + i]?.add(additional)
            }
            "round ${i}".print()
            cards
                .print()
        }
        return cards
            .print()
            .toList()
            .map { it.second.sum() }
            .print()
            .sum()
            .print()

        return "not yet implement"
    }
}