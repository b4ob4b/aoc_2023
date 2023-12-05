import utils.*

fun main() {
    Day05(IO.TYPE.SAMPLE).test(35L, 46L)
    Day05().solve()
}

class Day05(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("", inputType = inputType) {

    val data = input.split("\n\n")

    override fun part1(): Any? {
        val seeds = data.first().replace("seeds: ", "").split(" ").map { it.toLong() }
        val maps = data
            .drop(1)
            .map { it.splitLines().drop(1) }
            .map {
                it.map {
                    val line = it.split(" ")
                    val dest = line[0].toLong()
                    val source = line[1].toLong()
                    val range = line[2].toLong()
                    val destmap = dest..(dest + range - 1)
                    val sourcemap = source..(source + range - 1)
//                    mapOf(sourcemap to destmap)
                    destmap to sourcemap
                }
            }


        val l = maps.fold(seeds) { seeeds, map ->

            val x = seeeds.map { seed ->
                val m = map.filter { seed in it.second }
                if (m.isNotEmpty()) {
                    val delta = m.single().second.first - m.single().first.first
                    seed.toLong() - delta
                } else {
                    seed.toLong()
                }
            }
            x
        }
        return l.min()

        return "not yet implement"
    }

    private data class Mapper(val source: LongRange, val target: LongRange, val delta: Long)

    override fun part2(): Long {
        val seeds0 = data.first().replace("seeds: ", "").split(" ").map { it.toLong() }
        val seeds = seeds0.chunked(2).map { it[0]..(it[0] + it[1] - 1) }
        val mappersList = data
            .drop(1)
            .map { it.splitLines().drop(1) }
            .map {
                it.map {
                    val line = it.split(" ")
                    val dest = line[0].toLong()
                    val source = line[1].toLong()
                    val range = line[2].toLong()
                    val sourcemap = source..(source + range - 1)
                    val target = dest..(dest + range - 1)
                    Mapper(sourcemap, target, dest - source)
                }
            }
        var locationCandidate = 0L
        while (true) {
            val candidate = mappersList.reversed().fold(locationCandidate) { acc, mappers ->
                val mapper = mappers.filter { acc in it.target }.singleOrNull()
                if (mapper == null) {
                    acc
                } else {
                    acc - mapper.delta
                }
            }
            if (seeds.any { seed -> candidate in seed }) {
                return locationCandidate
            }
            locationCandidate += 1
            locationCandidate.print()
        }




        return 1
    }
}