import utils.Day
import utils.IO
import utils.extractToList
import utils.splitLines

fun main() {
    Day07(IO.TYPE.SAMPLE).test(6440L, 5905L)
    Day07().solve()
}

class Day07(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("Camel Cards", inputType = inputType) {

    private val handsData = input.splitLines()
        .map { it.split(" ") }
        .map { (cards, strength) ->
            cards.extractToList("\\w") to strength.toInt()
        }

    override fun part1() = handsData
        .map { (cards, strength) ->
            Hand1(cards, strength)
        }
        .sorted()
        .calculateTotalWinnings()

    override fun part2() = handsData
        .map { (cards, strength) ->
            Hand2(cards, strength)
        }
        .sorted()
        .calculateTotalWinnings()

    private fun List<Hand>.calculateTotalWinnings() = this
        .mapIndexed { rank, hand ->
            (rank + 1) * hand.strength.toLong()
        }.sum()

    abstract class Hand(open val cards: List<String>, open val strength: Int) : Comparable<Hand> {
        abstract val cardValues: Map<String, Int>
        fun String.calculateCardValues() = this.split(", ").reversed().mapIndexed { index, s -> s to index + 1 }.toMap()
        fun Map<String, Int>.hasTwoPairs() = this.values
            .groupingBy { it }
            .eachCount()
            .filter { it.value == 2 && it.key == 2 }
            .isNotEmpty()

        override fun compareTo(other: Hand): Int {
            if (this.calculateType() > other.calculateType()) return 1
            if (this.calculateType() < other.calculateType()) return -1
            val matchingCards = cards.indices
                .takeWhile { cards[it] == other.cards[it] }.lastOrNull() ?: -1
            val firstUnMatchedCard = matchingCards + 1
            if (firstUnMatchedCard == 5) return 0
            return if (cardValues[cards[firstUnMatchedCard]]!! > cardValues[other.cards[firstUnMatchedCard]]!!)
                1 else -1
        }

        abstract fun calculateType(): Int

    }

    data class Hand1(override val cards: List<String>, override val strength: Int) : Hand(cards, strength) {
        override val cardValues = "A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, 2".calculateCardValues()

        override fun calculateType(): Int {
            val groupedCards = cards.groupingBy { it }.eachCount()
            val cardsOfAKind = groupedCards.values.max()
            val hasTwoPairs = groupedCards.hasTwoPairs()
            return when {
                cardsOfAKind == 5 -> 7
                cardsOfAKind == 4 -> 6
                groupedCards.values.containsAll(listOf(2, 3)) -> 5
                cardsOfAKind == 3 -> 4
                hasTwoPairs -> 3
                cardsOfAKind == 2 -> 2
                else -> 1
            }
        }
    }

    data class Hand2(override val cards: List<String>, override val strength: Int) : Hand(cards, strength) {
        override val cardValues = "A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2, J".calculateCardValues()

        override fun calculateType(): Int {
            val groupedCards = cards.groupingBy { it }.eachCount()
            val amountJ = groupedCards["J"] ?: 0
            val cardsOfAKind = (groupedCards.filterKeys { it != "J" }.values.maxOrNull() ?: 0) + amountJ
            val hasTwoPairs = groupedCards.hasTwoPairs()

            return when {
                cardsOfAKind == 5 -> 7
                cardsOfAKind == 4 -> 6
                groupedCards.values.containsAll(listOf(2, 3)) -> 5
                hasTwoPairs && amountJ == 1 -> 5
                cardsOfAKind == 3 -> 4
                hasTwoPairs -> 3
                cardsOfAKind == 2 -> 2
                else -> 1
            }
        }
    }
}