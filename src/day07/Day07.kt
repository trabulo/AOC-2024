package day07

import println
import readInput
import java.math.BigInteger
import kotlin.math.pow

private const val PATH_RESOURCES = "day07/resources"

fun main() {

    fun part1(input: List<String>): BigInteger {

        return Solution1(equations = input.toEquations()).calibrate()
    }

    fun part2(input: List<String>): BigInteger {

        return Solution2(equations = input.toEquations()).calibrate()
    }

    // Test if implementation meets criteria for Part01:
    val result01 = part1(readInput("$PATH_RESOURCES/Day07_01_01"))
    check(result01 == 3749.toBigInteger())

    val result02 = part1(readInput("$PATH_RESOURCES/Day07_01_02"))
    check(result02 == 2299996598890.toBigInteger())

    // Test if implementation meets criteria for Part02:
    val result03 = part2(readInput("$PATH_RESOURCES/Day07_02_01"))
    check(result03 == 11387.toBigInteger())

    val result04 = part2(readInput("$PATH_RESOURCES/Day07_02_02"))
    check(result04 == 362646859298554.toBigInteger())

    // Output results:
    result02.println()
    result04.println()
}

private class Solution1(val equations: List<Equation>) {

    fun Equation.isValid(): Boolean {

        val combinations = 2.0.pow(operands.size - 1).toInt()
        for (combination in 0 until combinations) {

            var value = operands[0]

            val operations = combination.toString(radix = 2).padStart(length = operands.size - 1, '0')
            for (i in 1..operands.lastIndex) {

                when (operations[i - 1]) {

                    '0' -> value += operands[i]
                    '1' -> value *= operands[i]
                }
            }

            if (result == value) {

                return true
            }
        }

        return false
    }

    fun calibrate(): BigInteger {

        return equations.filter { equation ->

            equation.isValid()

        }.sumOf(Equation::result)
    }
}

private class Solution2(val equations: List<Equation>) {

    fun Equation.isValid(): Boolean {

        val combinations = 3.0.pow(operands.size - 1).toInt()
        for (combination in 0 until combinations) {

            var value = operands[0]

            val operations = combination.toString(radix = 3).padStart(length = operands.size - 1, '0')
            for (i in 1..operands.lastIndex) {

                when (operations[i - 1]) {

                    '0' -> value += operands[i]
                    '1' -> value *= operands[i]
                    '2' -> value = BigInteger("$value${operands[i]}")
                }
            }

            if (result == value) {

                return true
            }
        }

        return false
    }

    fun calibrate(): BigInteger {

        return equations.filter { equation ->

            equation.isValid()

        }.sumOf(Equation::result)
    }
}

private fun List<String>.toEquations(): List<Equation> {

    val digits = "\\d+".toRegex()

    return map { line ->

        val numbers = digits.findAll(line).map { result ->

            BigInteger(result.value)

        }.toList()

        Equation(result = numbers[0], operands = numbers - numbers[0])
    }
}

private class Equation(val result: BigInteger, val operands: List<BigInteger>)

