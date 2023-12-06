import utils.*

fun main() {
    Day05(IO.TYPE.SAMPLE).test(35L, 46L)
    Day05().solve()
}

class Day05(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("If You Give A Seed A Fertilizer", inputType = inputType) {

    private val groups = input.split("\n\n")
    private val seedNumbers = groups.first().replace("seeds: ", "").split(" ")

    private data class Mapper(val source: LongRange, val target: LongRange, val delta: Long)

    private val mappingList = groups
        .drop(1)
        .map { it.splitLines().drop(1) }
        .map { group ->
            group.map { mapInstruction ->
                val (dest, source, range) = mapInstruction.split(" ").map { it.toLong() }
                val sourcemap = source..<source + range
                val target = dest..<dest + range
                Mapper(sourcemap, target, dest - source)
            }
        }

    override fun part1(): Long {
        val seeds = seedNumbers.map { it.toLong() }
        return mappingList.fold(seeds) { nextSeeds, map ->
            nextSeeds.map { seed ->
                val delta = map.singleOrNull { seed in it.source }?.delta ?: 0
                seed + delta
            }
        }.min()
    }

    override fun part2(): Long {
        val seeds = seedNumbers
            .map { it.toLong() }
            .chunked(2)
            .map { (start, range) -> start..<start + range }

        var locationCandidate = 0L
        while (true) {
            val candidate = mappingList.reversed().fold(locationCandidate) { acc, mappers ->
                val delta = mappers.singleOrNull { acc in it.target }?.delta ?: 0
                acc - delta
            }
            if (seeds.any { seed -> candidate in seed }) {
                return locationCandidate
            }
            locationCandidate += 1
        }

    }
}