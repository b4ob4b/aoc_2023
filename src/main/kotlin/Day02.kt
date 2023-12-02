import utils.*

fun main() {
    Day02(IO.TYPE.SAMPLE).test(8, 2286)
    Day02().solve()
}

class Day02(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("Cube Conundrum", inputType = inputType) {

    private val data = input.splitLines()
    private val colors = listOf("red", "blue", "green")
    private val elf = mapOf(
        "red" to 12,
        "green" to 13,
        "blue" to 14,
    )


    override fun part1(): Any? {
        return data.mapIndexedNotNull { index, game ->
            val isGamePossible = colors
                .all { color ->
                    game.findMaxCubesOf(color)!! < elf[color]!!
                }

            if (isGamePossible) index + 1 else null
        }.sum()
    }

    override fun part2(): Any? {
        return data.sumOf { game ->
            colors.mapNotNull { color ->
                game.findMaxCubesOf(color)
            }.product()
        }
    }

    private fun String.findAllPositionsOf(pattern: String) = Regex(pattern)
        .findAll(this)
        .map {
            it.range.first
        }.toList()

    private fun String.findMaxCubesOf(color: String) = this
        .findAllPositionsOf(color).maxOfOrNull { position ->
            this
                .subSequence(position - 3 until position - 1)
                .removePrefix(" ")
                .toString()
                .toInt()
        }
}