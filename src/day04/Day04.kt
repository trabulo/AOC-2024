package day04

import day04.Solution2.Direction.*
import println
import readInput

private const val PATH_RESOURCES = "day04/resources"

fun main() {

    fun part1(input: List<String>): Int {

        return Solution1(grid = Grid(lines = input)).count()
    }

    fun part2(input: List<String>): Int {

        return Solution2(grid = Grid(lines = input)).count()
    }

    // Test if implementation meets criteria for Part01:
    val result01 = part1(readInput("$PATH_RESOURCES/Day04_01_01"))
    check(result01 == 18)

    val result02 = part1(readInput("$PATH_RESOURCES/Day04_01_02"))
    check(result02 == 2532)

    // Test if implementation meets criteria for Part02:
    val result03 = part2(readInput("$PATH_RESOURCES/Day04_02_01"))
    check(result03 == 9)

    val result04 = part2(readInput("$PATH_RESOURCES/Day04_02_02"))
    check(result04 == 1941)

    // Output results:
    result02.println()
    result04.println()
}

private data class Solution1(private val grid: Grid) {

    private enum class Direction(val x: Int, val y: Int) {

        NN( 0, -1),
        EE( 1,  0),
        SS( 0,  1),
        WW(-1,  0),
        NE( 1, -1),
        SE( 1,  1),
        SW(-1,  1),
        NW(-1, -1)
    }

    fun count(): Int {

        val characters = "XMAS".toCharArray()

        return grid.sumOf { x, y ->

            if (isValid(x, y) && valueAt(x, y) == characters[0]) {

                Direction.entries.count { direction ->

                    var matches = 1

                    var m = x + direction.x
                    var n = y + direction.y

                    for (i in 1 until characters.size) {

                        if (isValid(m, n) && valueAt(m, n) == characters[i]) {

                            matches++

                            m += direction.x
                            n += direction.y

                        } else break
                    }

                    matches == characters.size
                }

            } else 0
        }
    }
}

private data class Solution2(private val grid: Grid) {

    private enum class Direction(val x: Int, val y: Int) {

        NE( 1, -1),
        SE( 1,  1),
        SW(-1,  1),
        NW(-1, -1)
    }

    private fun isValid(data: Map<Direction, Char>): Boolean {

        return data.size == 4 && data[NE] != data[SW] && data[NW] != data[SE]
    }

    fun count(): Int {

        return grid.sumOf { x, y ->

            if (isValid(x, y) && valueAt(x, y) == 'A') {

                val matches = hashMapOf<Direction, Char>()

                Direction.entries.forEach { direction ->

                    val m = x + direction.x
                    val n = y + direction.y

                    if (isValid(m, n) && (valueAt(m, n) == 'M' || valueAt(m, n) == 'S')) {

                        matches[direction] = valueAt(m, n)
                    }
                }

                if (isValid(data = matches)) 1 else 0

            } else 0
        }
    }
}

private fun Grid.sumOf(block: Grid.(Int, Int) -> Int): Int {

    var total = 0

    for (x in 0 until rows()) {

        for (y in 0 until columns()) {

            total += block(x, y)
        }
    }

    return total
}

private data class Grid(private val lines: List<String>) {

    fun isValid(x: Int, y: Int): Boolean {

        return x in 0 until columns() && y in 0 until rows()
    }

    fun valueAt(x: Int, y: Int): Char {

        return lines[x][y]
    }

    fun columns(): Int {

        return lines[0].length
    }

    fun rows(): Int {

        return lines.size
    }
}