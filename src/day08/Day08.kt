package day08

import println
import readInput

private const val PATH_RESOURCES = "day08/resources"

fun main() {

    fun part1(input: List<String>): Int {

        return Solution1(grid = Grid(data = input)).count()
    }

    fun part2(input: List<String>): Int {

        return Solution2(grid = Grid(data = input)).count()
    }

    // Test if implementation meets criteria for Part01:
    val result01 = part1(readInput("$PATH_RESOURCES/Day08_01_01"))
    check(result01 == 14)

    val result02 = part1(readInput("$PATH_RESOURCES/Day08_01_02"))
    check(result02 == 359)

    // Test if implementation meets criteria for Part02:
    val result03 = part2(readInput("$PATH_RESOURCES/Day08_02_01"))
    check(result03 == 34)

    val result04 = part2(readInput("$PATH_RESOURCES/Day08_02_02"))
    check(result04 == 1293)

    // Output results:
    result02.println()
    result04.println()
}

private class Solution1(val grid: Grid) {

    fun count(): Int {

        val nodes = LinkedHashSet<Position>()
        grid.lookup().forEach { (_, positions) ->

            for (i in 0 until positions.size) {

                val position1 = positions[i]

                for (j in (i + 1) until positions.size) {

                    val position2 = positions[j]

                    val vector = position2 - position1

                    val node1 = position1 - vector
                    val node2 = position2 + vector

                    if (grid.contains(node1)) nodes += node1
                    if (grid.contains(node2)) nodes += node2
                }
            }
        }

        return nodes.size
    }
}

private class Solution2(val grid: Grid) {

    fun count(): Int {

        val signs = arrayOf(-1, +1)

        val nodes = LinkedHashSet<Position>()
        grid.lookup().forEach { (_, positions) ->

            for (i in 0 until positions.size) {

                val position1 = positions[i]

                for (j in i + 1 until positions.size) {

                    val position2 = positions[j]

                    val vector = (position2 - position1)
                    nodes += position1 // t = 0

                    signs.forEach { sign ->

                        var t = 1

                        var node = position1 + vector * t * sign
                        while (grid.contains(node)) {

                            nodes += node

                            t++
                            node = position1 + vector * t * sign
                        }
                    }
                }
            }
        }

        return nodes.size
    }
}

private operator fun Position.minus(that: Position): Position {

    return Position(this.x - that.x, this.y - that.y)
}

private operator fun Position.plus(that: Position): Position {

    return Position(this.x + that.x, this.y + that.y)
}

private operator fun Position.times(scalar: Int): Position {

    return Position(this.x * scalar, this.y * scalar)
}

private fun Grid.lookup(): Map<Char, ArrayList<Position>> {

    return buildMap {

        for (x in 0 until cols()) {

            for (y in 0 until rows()) {

                val position = Position(x, y)

                val value = value(position = position)
                if (value == '.') continue else {

                    val positions = getOrDefault(value, ArrayList())
                    positions += position

                    put(value, positions)
                }
            }
        }
    }
}

private data class Position(val x: Int, val y: Int)

private data class Grid(val data: List<String>) {

    private val rows by lazy { data.size }
    private val cols by lazy { data[0].length }

    fun contains(position: Position): Boolean {

        return position.x in 0 until cols && position.y in 0 until rows
    }

    fun value(position: Position): Char {

        return data[position.y][position.x]
    }

    fun cols(): Int {

        return cols
    }

    fun rows(): Int {

        return rows
    }
}

