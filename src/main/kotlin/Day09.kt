import utils.Day
import utils.IO
import utils.extractInts
import utils.splitLines

fun main() {
    Day09(IO.TYPE.SAMPLE).test(114, 2)
    Day09().solve()
}

class Day09(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("Mirage Maintenance", inputType = inputType) {

    private val sensorData = input.splitLines().map { it.extractInts() }

    override fun part1() = sensorData.sumOf { it.calculateFuture() }

    override fun part2() = sensorData.sumOf { it.calculatePast() }

    private fun List<Int>.calculateFuture(): Int {
        if (this.all { it == 0 }) return 0
        val futureValue = this.windowed(2).map { it[1] - it[0] }.calculateFuture()
        return this.last() + futureValue
    }

    private fun List<Int>.calculatePast() = this.reversed().calculateFuture()

}