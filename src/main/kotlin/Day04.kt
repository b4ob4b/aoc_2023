import utils.*

fun main() {
    Day04(IO.TYPE.SAMPLE).test(13, 30)
    Day04().solve()
}

class Day04(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("Scratchcards", inputType = inputType) {

    private val wins = input.splitLines()
        .map { it.split(": |\\|".toRegex()).drop(1) }
        .map { game -> game.map { it.extractInts(" ") } }
        .map { it[0].toSet() intersect it[1].toSet() }
        .map { it.size }

    override fun part1(): Int {
        val pointConversion = listOf(0) + generateSequence(1) { it * 2 }.take(10).toList()
        return wins
            .sumOf { pointConversion[it] }
    }

    override fun part2(): Int {
        val cards = MutableList(wins.size) { 0 }
        for (round in wins.indices) {
            cards[round] += 1
            for (copy in 1..wins[round]) {
                cards[copy + round] += cards[round]
            }
        }
        return cards
            .sum()
    }
}