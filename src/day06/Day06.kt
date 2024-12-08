package day06

import day06.Direction.*
import println
import readInput

private const val GUARD = '^'

private const val OBSTACLE = '#'

private const val PATH_RESOURCES = "day06/resources"

fun main() {

    fun part1(input: List<String>): Int {

        return Solution1(grid = Grid(data = input)).simulate()
    }

    fun part2(input: List<String>): Int {

        return Solution2(grid = Grid(data = input)).simulate()
    }

    // Test if implementation meets criteria for Part01:
    val result01 = part1(readInput("$PATH_RESOURCES/Day06_01_01"))
    check(result01 == 41)

    val result02 = part1(readInput("$PATH_RESOURCES/Day06_01_02"))
    check(result02 == 4758)

    // Test if implementation meets criteria for Part02:
    val result03 = part2(readInput("$PATH_RESOURCES/Day06_02_01"))
    check(result03 == 6)

    val result04 = part2(readInput("$PATH_RESOURCES/Day06_02_02"))
    check(result04 == 1670)

    // Output results:
    result02.println()
    result04.println()
}

private data class Solution1(private val grid: Grid) {

    fun simulate(): Int {

        var current = grid.findGuard()

        var direction = N
        var candidate = current + direction

        val visited = LinkedHashSet<Position>()
        while (grid.contains(position = candidate)) {

            if (grid.value(position = candidate) == OBSTACLE) {

                direction = direction.turn()

            } else {

                visited += candidate
                current = candidate
            }

            candidate = current + direction
        }

        return visited.size
    }
}

private data class Solution2(private val grid: Grid) {

    fun simulate(): Int {

        val initial = grid.findGuard()

        var counter = 0
        for (i in 0 until grid.cols()) {

            for (j in 0 until grid.rows()) {

                // Test Obstacle
                val obstacle = Position(x = i, y = j)

                // Ignore guard position and already existing obstacles
                if (initial != obstacle && grid.value(obstacle) != OBSTACLE) {

                    var current = initial

                    var direction = N
                    var candidate = current + direction

                    val visited = LinkedHashSet<Pair<Position, Direction>>()
                    while (grid.contains(position = candidate)) {

                        if (candidate to direction in visited) {
                            counter++
                            break
                        }

                        if (grid.value(position = candidate) == OBSTACLE || candidate == obstacle) {

                            direction = direction.turn()

                        } else {

                            visited += candidate to direction
                            current = candidate
                        }

                        candidate = current + direction
                    }
                }
            }
        }

        return counter
    }
}

private enum class Direction(val x: Int, val y: Int) {

    N( 0, -1),
    S( 0,  1),
    E( 1,  0),
    W(-1,  0)
}

private fun Direction.turn(): Direction {

    return when (this) {
        N -> E
        S -> W
        E -> S
        W -> N
    }
}

private data class Position(val x: Int, val y: Int)

private operator fun Position.plus(direction: Direction): Position {

    return Position(x + direction.x, y + direction.y)
}

private fun Grid.findGuard(): Position {

    for (x in 0 until cols()) {

        for (y in 0 until rows()) {

            val position = Position(x, y)
            if (value(position) == GUARD) {

                return position
            }
        }
    }

    error("Can't find the guard position")
}

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