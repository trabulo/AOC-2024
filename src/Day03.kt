import java.util.regex.Pattern

fun main() {

    val regex1 = Pattern.compile("mul\\((\\d+),(\\d+)\\)").toRegex()
    val regex2 = Pattern.compile("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don\'t\\(\\)").toRegex()

    fun MatchResult.toResult(): Int {

        val (a, b) = destructured
        return a.toInt() * b.toInt()
    }

    fun part1(input: List<String>): Int {

        return input.sumOf { line ->

            regex1.findAll(line).sumOf(MatchResult::toResult)
        }
    }

    fun part2(input: List<String>): Int {

        var enabled = true
        return input.sumOf { line ->

            regex2.findAll(line).sumOf { match ->

                when (match.value) {

                    "do()"    -> { enabled = true;  0 }
                    "don't()" -> { enabled = false; 0 }
                    else -> if (enabled) match.toResult() else 0
                }
            }
        }
    }

    // Test if implementation meets criteria for Part01:
    val result01 = part1(readInput("resources/Day03_01_01"))
    check(result01 == 161)

    val result02 = part1(readInput("resources/Day03_01_02"))
    check(result02 == 187825547)

    // Test if implementation meets criteria for Part02:
    val result03 = part2(readInput("resources/Day03_02_01"))
    check(result03 == 48)

    val result04 = part2(readInput("resources/Day03_02_02"))
    check(result04 == 85508223)

    // Output results:
    result02.println()
    result04.println()
}