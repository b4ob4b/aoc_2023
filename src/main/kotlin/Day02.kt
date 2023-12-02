import utils.*

fun main() {
    Day02(IO.TYPE.SAMPLE).test(8, 2286)
    Day02().solve()
}
//12 red cubes, 13 green cubes, and 14 blue cubes
//Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green

class Day02(inputType: IO.TYPE = IO.TYPE.INPUT) : Day("", inputType = inputType) {

    data class Game(
        val id: Int,
        val red: Int,
        val blue: Int,
        val green: Int,
    )

    private val elf = Game(0, 12, 14, 13)

    val data = input.splitLines()
    override fun part1(): Any? {
        val red = data
            .map {
                it.split(" red").mapNotNull {
                    val l = it.length
                    if (l >= 2) it.subSequence(l - 2..l - 1).toString()
                        .replace(" ", "")

                        .toIntOrNull() else null
                }.max()

            }.mapIndexed { index, i ->
                if (i > elf.red) index + 1 else null
            }.filterNotNull()
        val blue = data
            .map {
                it.split(" blue").mapNotNull {
                    val l = it.length
                    if (l >= 2) it.subSequence(l - 2..l - 1).toString()
                        .replace(" ", "")

                        .toIntOrNull() else null
                }.max()

            }.mapIndexed { index, i ->
                if (i > elf.blue) index + 1 else null
            }.filterNotNull()
        val green = data
            .map {
                it.split(" green").mapNotNull {
                    val l = it.length
                    if (l >= 2) it.subSequence(l - 2..l - 1).toString()
                        .replace(" ", "")

                        .toIntOrNull() else null
                }.max()

            }.mapIndexed { index, i ->
                if (i > elf.green) index + 1 else null
            }.filterNotNull()

        val r = mutableSetOf<Int>()
        r.addAll(1..data.size)
        r.removeAll(red)
        r.removeAll(blue)
        r.removeAll(green)
        return r.sum()
        return "not yet implement"
    }

    override fun part2(): Any? {
        val red = data
            .map {
                it.split(" red").mapNotNull {
                    val l = it.length
                    if (l >= 2) it.subSequence(l - 2..l - 1).toString()
                        .replace(" ", "")

                        .toIntOrNull() else null
                }.max()

            }.print()
        val blue = data
            .map {
                it.split(" blue").mapNotNull {
                    val l = it.length
                    if (l >= 2) it.subSequence(l - 2..l - 1).toString()
                        .replace(" ", "")

                        .toIntOrNull() else null
                }.max()

            }
        val green = data
            .map {
                it.split(" green").mapNotNull {
                    val l = it.length
                    if (l >= 2) it.subSequence(l - 2..l - 1).toString()
                        .replace(" ", "")

                        .toIntOrNull() else null
                }.max()

            }
        return zip(red, green, blue)
            .map { it.product() }
            .sum()
            .print()
    }

    public inline fun <T> zip(vararg lists: List<T>): List<List<T>> {
        return zip(*lists, transform = { it })
    }

    public inline fun <T, V> zip(vararg lists: List<T>, transform: (List<T>) -> V): List<V> {
        val minSize = lists.map(List<T>::size).min() ?: return emptyList()
        val list = ArrayList<V>(minSize)

        val iterators = lists.map { it.iterator() }
        var i = 0
        while (i < minSize) {
            list.add(transform(iterators.map { it.next() }))
            i++
        }

        return list
    }
}