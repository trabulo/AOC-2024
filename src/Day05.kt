import java.util.*

fun main() {

    fun part1(input: List<String>): Int {

        val updates = input.toUpdates()
        return updates.sumOf { update ->

            if (update.isValid()) {

                update.value()

            } else 0
        }
    }

    fun part2(input: List<String>): Int {

        val updates = input.toUpdates()
        return updates.filterNot(Update::isValid).sumOf { update ->

            val sorted = update.sort()
            if (sorted.isValid()) {

                sorted.value()

            } else 0
        }
    }

    // Test if implementation meets criteria for Part01:
    val result01 = part1(readInput("resources/Day05_01_01"))
    check(result01 == 143)

    val result02 = part1(readInput("resources/Day05_01_02"))
    check(result02 == 5166)

    // Test if implementation meets criteria for Part02:
    val result03 = part2(readInput("resources/Day05_02_01"))
    check(result03 == 123)

    val result04 = part2(readInput("resources/Day05_02_02"))
    check(result04 == 4679)

    // Output results:
    result02.println()
    result04.println()
}

data class Rule(val u: Int, val v: Int)

data class Update(val pages: List<Int>, val rules: List<Rule>) {

    fun isValid(): Boolean {

        return rules.all { rule ->

            pages.isValid(rule = rule)
        }
    }

    private fun List<Int>.isValid(rule: Rule): Boolean {

        val i = indexOf(rule.u) // before
        val j = indexOf(rule.v) // after

        return if (i == -1 || j == -1) true else i < j
    }

    fun value(): Int {

        return pages[pages.lastIndex / 2]
    }

    @Suppress("unused")
    fun correct(): Update {

        val corrected = pages.toMutableList()
        val result = Update(pages = corrected, rules = rules)

        // Remark: How to avoid potential endless loop? Use topological sorting.
        while (!result.isValid()) {

            for (rule in rules) {

                if (corrected.isValid(rule = rule)) continue else {

                    val i = corrected.indexOf(rule.u)
                    val j = corrected.indexOf(rule.v)

                    corrected[i] = rule.v
                    corrected[j] = rule.u

                    check(corrected.isValid(rule))
                }
            }
        }

        return result
    }
}

private fun Update.sort(): Update {

    val digraph = LinkedHashMap<Int, List<Int>>()
    val degrees = LinkedHashMap<Int, Int>()

    val nodes = LinkedHashSet(pages)
    nodes.forEach { node ->

        degrees[node] = 0
    }

    rules.forEach { rule ->

        if (nodes.contains(rule.u) && nodes.contains(rule.v)) {

            digraph[rule.u] = digraph.getOrDefault(rule.u, emptyList()) + rule.v
            degrees[rule.v] = degrees.getOrDefault(rule.v, 0) + 1
        }
    }

    val queue = LinkedList<Int>()
    nodes.forEach { node ->

        if (degrees[node] == 0) {

            queue.offer(node)
        }
    }

    val result = ArrayList<Int>()
    while (queue.isNotEmpty()) {

        val node = queue.poll()
        result += node

        for (successor in digraph[node].orEmpty()) {

            val value = degrees.getOrDefault(successor, 0) - 1
            degrees[successor] = value

            if (value == 0) {

                queue.offer(successor)
            }
        }
    }

    // Remark: check if cycle detected or unable to sort
    return if (result.size == nodes.size) {

        copy(pages = result)

    } else this
}

private fun List<String>.toUpdates(): List<Update> {

    val ruleset = ArrayList<Rule>()
    val updates = ArrayList<Update>()

    forEach { entry ->

        when {
            entry.contains("|") -> ruleset += entry.toRule()
            entry.contains(",") -> updates += entry.toUpdate(rules = ruleset)
        }
    }

    return updates
}

private fun String.toRule(): Rule {

    val (u, v) = split("|").map(String::toInt)
    return Rule(u = u, v = v)
}

private fun String.toUpdate(rules: List<Rule>): Update {

    val pages = split(",").map(String::toInt)
    return Update(pages = pages, rules = rules)
}