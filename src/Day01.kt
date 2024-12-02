import java.util.regex.Pattern
import kotlin.math.abs

fun main() {

    val regex = Pattern.compile("\\s+").toRegex()

    fun part1(input: List<String>): Int {

        val (lColumn, rColumn) = input.map { line ->

            val matches = line.split(regex = regex, limit = 2)
            matches[0].toInt() to matches[1].toInt()

        }.unzip()

        val lSorted = lColumn.sorted()
        val rSorted = rColumn.sorted()

        return lSorted.sorted().zip(rSorted.sorted()).sumOf {

            abs(it.second - it.first)
        }
    }

    fun part2(input: List<String>): Int {

        val (lColumn, rColumn) = input.map { line ->

            val matches = line.split(regex = regex, limit = 2)
            matches[0].toInt() to matches[1].toInt()

        }.unzip()

        val frequencies = rColumn.groupingBy { it }.eachCount()
        return lColumn.sumOf { value ->

            value * (frequencies[value] ?: 0)
        }
    }

    // Test if implementation meets criteria for Part01:
    val result01 = part1(readInput("resources/Day01_01_01"))
    check(result01 == 11)

    val result02 = part1(readInput("resources/Day01_01_02"))
    check(result02 == 2970687)

    // Test if implementation meets criteria for Part02:
    val result03 = part2(readInput("resources/Day01_02_01"))
    check(result03 == 31)

    val result04 = part2(readInput("resources/Day01_02_02"))
    check(result04 == 23963899)

    // Output results:
    result02.println()
    result04.println()
}
