import utils.Day
import utils.IO
import utils.print
import utils.splitLines

fun main() {
    Day01(IO.TYPE.SAMPLE).test(part1 = 142)
    Day01(IO.TYPE.SAMPLE2).test(part2 = 281)
    Day01().solve()
}

class Day01(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("", inputType = inputType) {

    private val data = input.splitLines()
    private val numbers = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
    )

    override fun part1(): Int {
        val tens = data.mapNotNull { it.findDigit() }
        val ones = data.mapNotNull { it.reversed().findDigit() }

        return calculateCalibrationValue(tens, ones)
    }
    
    override fun part2(): Int {
        val patternForTens = numbers.keys.joinToString("|") + "|\\d"
        val patternForOnes = numbers.keys.joinToString("|") { it.reversed() } + "|\\d"
        val tens = data.mapNotNull { it.findDigit(patternForTens) }
        val ones = data.mapNotNull { it.reversed().findDigit(patternForOnes, true) }

        return calculateCalibrationValue(tens, ones)
    }

    private fun String.findDigit(pattern: String = "\\d", reversed: Boolean = false): Int? {
        val match = Regex(pattern).find(this)?.value
        return when {
            match?.length == 1 -> match.toInt()
            reversed -> numbers[match?.reversed()]
            else -> numbers[match]
        }
    }

    private fun calculateCalibrationValue(tens: List<Int>, ones: List<Int>) = tens.zip(ones).sumOf { it.first * 10 + it.second }
}