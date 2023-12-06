import utils.*

fun main() {
    Day06(IO.TYPE.SAMPLE).test(288, 71503)
    Day06().solve()
}

class Day06(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("Wait For It", inputType = inputType) {

    data class Race(val time: Int, val distance: Long)

    private val races = input.splitLines()
        .map {
            it.extractInts(" +".toRegex())
        }.let { (times, distances) ->
            times.zip(distances)
        }.map { Race(it.first, it.second.toLong()) }

    private val race2 = input.splitLines()
        .map { it.replace("\\D".toRegex(), "") }
        .let { (time, distance) -> Race(time.toInt(), distance.toLong()) }

    override fun part1() = races
        .map { race -> simulateRaces(race) }
        .product()

    override fun part2() = simulateRaces(race2)

    private fun simulateRaces(race: Race): Int {
        return (1..race.time)
            .count { speed ->
                val remaining = race.time - speed
                val result = remaining * speed.toLong()
                result > race.distance
            }
    }
}