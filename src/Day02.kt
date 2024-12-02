import java.util.regex.Pattern
import kotlin.math.abs

private const val MIN_LEVEL = 1
private const val MAX_LEVEL = 3

fun main() {

    val regex = Pattern.compile("\\s+").toRegex()

    fun Int.isValid(): Boolean {

        return abs(this) in MIN_LEVEL..MAX_LEVEL
    }

    fun List<Int>.isIncreasing(): Boolean {

        return all { it > 0 }
    }

    fun List<Int>.isDecreasing(): Boolean {

        return all { it < 0 }
    }

    fun List<Int>.isSafe(): Boolean {

        return all(Int::isValid) && (isIncreasing() || isDecreasing())
    }

    fun part1(input: List<String>): Int {

        return input.count { line ->

            val matches = line.split(regex = regex)
            matches.zipWithNext { a, b ->

                b.toInt() - a.toInt()

            }.isSafe()
        }
    }

    fun part2(input: List<String>): Int {

        return input.count { line ->

            val matches = line.split(regex = regex)
            matches.indices.any { ignore ->

                val filtered = matches.filterIndexed { index, _ -> index != ignore }
                filtered.zipWithNext { a, b ->

                    b.toInt() - a.toInt()

                }.isSafe()
            }
        }
    }

    // Test if implementation meets criteria for Part01:
    val result01 = part1(readInput("resources/Day02_01_01"))
    check(result01 == 2)

    val result02 = part1(readInput("resources/Day02_01_02"))
    check(result02 == 224)

    val result03 = part2(readInput("resources/Day02_02_01"))
    check(result03 == 4)

    val result04 = part2(readInput("resources/Day02_02_02"))
    check(result04 == 293)

    // Output results:
    result02.println()
    result04.println()
}
