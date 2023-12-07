import utils.Day
import utils.IO
import utils.splitLines

fun main() {
    Day07(IO.TYPE.SAMPLE).test(6440L, 5905L)
    Day07().solve()
}

class Day07(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("Camel Cards", inputType = inputType) {
    private val data = input.splitLines()

    data class Round(val cards: List<String>, val rank: Int) : Comparable<Round> {
        private val cardValues = "A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2".split(", ").reversed().mapIndexed { index, s -> s to index + 2 }.toMap()

        override fun compareTo(other: Round): Int {
            val groups = this.cards.groupingBy { it }.eachCount()
            val groups2 = other.cards.groupingBy { it }.eachCount()
            if (groups.calculateType() > groups2.calculateType()) return 1
            if (groups.calculateType() < groups2.calculateType()) return -1
            val matchingCards = cards.indices
                .takeWhile { cards[it] == other.cards[it] }.lastOrNull() ?: -1
            val firstUnMatchedCard = matchingCards + 1
            if (firstUnMatchedCard == 5) return 0
            return if (cardValues[cards[firstUnMatchedCard]]!! > cardValues[other.cards[firstUnMatchedCard]]!!)
                1 else -1
        }

        private fun Map<String, Int>.calculateType(): Int {
            if (this.values.size == 1 && this.values.single() == 5) return 7
            if (this.values.contains(4)) return 6
            if (this.values.containsAll(listOf(2, 3))) return 5
            if (this.values.contains(3)) return 4
            val hasTwoPairs = this.values.groupingBy { it }.eachCount().filter { it.value == 2 && it.key == 2 }
                .isNotEmpty()
            if (hasTwoPairs) return 3
            if (this.values.contains(2)) return 2
            return 1
        }
    }

    data class Round2(val cards: List<String>, val rank: Int) : Comparable<Round2> {
        private val cardValues = "A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2, J".split(", ").reversed().mapIndexed { index, s -> s to index + 1 }.toMap()

        override fun compareTo(other: Round2): Int {
            val groups = this.cards.groupingBy { it }.eachCount()
            val groups2 = other.cards.groupingBy { it }.eachCount()
            if (groups.calculateType() > groups2.calculateType()) return 1
            if (groups.calculateType() < groups2.calculateType()) return -1
            val matchingCards = cards.indices
                .takeWhile { cards[it] == other.cards[it] }.lastOrNull() ?: -1
            val firstUnMatchedCard = matchingCards + 1
            if (firstUnMatchedCard == 5) return 0
            return if (cardValues[cards[firstUnMatchedCard]]!! > cardValues[other.cards[firstUnMatchedCard]]!!)
                1 else -1
        }

        fun calcType() = this.cards.groupingBy { it }.eachCount().calculateType()

        private fun Map<String, Int>.calculateType(): Int {
            val amountJ = this["J"] ?: 0
            val maxCards = (this.filterKeys { it != "J" }.values.maxOrNull() ?: 0) + amountJ
            val hasTwoPairs = this.filterKeys { it != "J" }.values.groupingBy { it }.eachCount().filter { it.value == 2 && it.key == 2 }
                .isNotEmpty()
//            "amount: $maxCards".print()
//            five
            if (amountJ == 5) return 7
            if (maxCards == 5) return 7
//            four
            if (maxCards == 4) return 6
//            FullHouse
            if (this.values.containsAll(listOf(2, 3))) return 5
            if (hasTwoPairs && amountJ == 1) return 5
//            three
            if (maxCards == 3) return 4
//            two pair
            if (hasTwoPairs) return 3
//            one pair
            if (maxCards == 2) return 2
            return 1
        }
    }

    override fun part1(): Long {
        val rounds = data
            .map { it.split(" ") }
            .map { (cards, rank) ->

                Round(cards.split("").filter { it.isNotEmpty() }, rank.toInt())
            }
        return rounds
            .sortedBy { it }
            .mapIndexed { index, round ->
                (index + 1) * round.rank.toLong()
            }.sum()
    }

    override fun part2(): Long {

        val rounds = data
            .map { it.split(" ") }
            .map { (cards, rank) ->

                Round2(cards.split("").filter { it.isNotEmpty() }, rank.toInt())
            }

        return rounds
            .sortedBy { it }
            .mapIndexed { index, round ->
                (index + 1) * round.rank.toLong()
            }.sum()
    }
}