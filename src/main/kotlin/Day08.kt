import utils.*

fun main() {
    Day08(IO.TYPE.SAMPLE).test(6)
    Day08(IO.TYPE.SAMPLE2).test(part2 = 6L)
    Day08().solve()
}

class Day08(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("Haunted Wasteland", inputType = inputType) {

    private val data = input.split("\n\n")
    private val instructions = data.first().extractToList("\\w").let { generateSequence(it) { it }.flatten() }
    private val positionMap = data
        .drop(1).single().splitLines()
        .associate {
            val (from, left, right) = it.extractToList("\\w+")
            from to (left to right)
        }

    override fun part1() = navigateTheNetwork("AAA", "ZZZ").print()

    override fun part2() = positionMap.keys
        .filter { it.endsWith("A") }
        .map { startingPoint ->
            navigateTheNetwork(startingPoint, "Z")
        }
        .calculateCommonMultiple()


    private fun navigateTheNetwork(start: String, goal: String): Int {
        var position = start
        return instructions.takeWhile { instruction ->
            position = if (instruction == "R") {
                positionMap[position]!!.second
            } else {
                positionMap[position]!!.first
            }
            position.endsWith(goal).not()
        }.count() + 1
    }

    private fun List<Int>.calculateCommonMultiple(): Long {
        val maxNumber = this.maxOrNull() ?: return -1
        var multiple = maxNumber.toLong()
        while (true) {
            if (this.all { multiple % it == 0L }) {
                return multiple
            }
            multiple += maxNumber
        }
    }
}