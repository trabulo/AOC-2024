@file:Suppress("unused")

package day09

import day09.Block.File
import day09.Block.Free
import day09.Solution1.Slot
import println
import readInput
import java.util.*

private const val PATH_RESOURCES = "day09/resources"

fun main() {

    fun part1(input: List<String>): Long {

        return Solution1(blocks = input[0].toBlocks()).compact()
    }

    fun part2(input: List<String>): Long {

        return Solution2(blocks = input[0].toBlocks()).compact()
    }

    // Test if implementation meets criteria for Part01:
    val result01 = part1(readInput("$PATH_RESOURCES/Day09_01_01"))
    check(result01 == 1928L)

    val result02 = part1(readInput("$PATH_RESOURCES/Day09_01_02"))
    check(result02 == 6399153661894L)

    // Test if implementation meets criteria for Part02:
    val result03 = part2(readInput("$PATH_RESOURCES/Day09_02_01"))
    check(result03 == 2858L)

    val result04 = part2(readInput("$PATH_RESOURCES/Day09_02_02"))
    check(result04 == 6421724645083L)

    // Output results:
    result02.println()
    result04.println()
}

private data class Solution1(private val blocks: List<Block>) {

    private data class Slot(val index: Int, val size: Int)

    private fun Array<Block>.toSlots(): LinkedList<Slot> {

        val slots = LinkedList<Slot>()

        forEachIndexed { index, block ->

            if (block is Free) {

                slots += Slot(index = index, size = 1)
            }
        }

        return slots
    }

    fun compact(): Long {

        var checksum = 0L

        val disk = blocks.toTypedArray()

        val slots = disk.toSlots()
        for (i in disk.indices.reversed()) {

            if (disk[i] is File) {

                val m = slots.indexOfFirst { it.size > 0 }
                if (m != -1) {

                    val slot = slots[m]
                    if (slot.index < i) {

                        val j = slot.index

                        disk[j] = disk[i]
                        disk[i] = Free

                        slots.removeAt(m)
                        slots += Slot(index = i, size = 1)
                    }
                }
            }
        }

        disk.forEachIndexed { index, block ->

            if (block is File) {

                checksum += (index * block.id)
            }
        }

        return checksum
    }
}

private data class Solution2(private val blocks: List<Block>) {

    private data class Slot(val index: Int, val size: Int)

    private fun Array<Block>.toSlots(): LinkedList<Slot> {

        val slots = LinkedList<Slot>()

        var i = 0
        while (i in 0..lastIndex) {

            if (this[i] is Free) {

                for (j in i..lastIndex) {

                    if (this[j] is File) {

                        slots += Slot(index = i, size = j - i)
                        i = j
                        break
                    }
                }
            }

            i++
        }

        return slots
    }

    fun compact(): Long {

        var checksum = 0L

        val disk = blocks.toTypedArray()

        val slots = disk.toSlots()
        for (i in disk.indices.reversed()) {

            val block = disk[i]
            if (block is File) {

                val m = slots.indexOfFirst { it.size >= block.size }
                if (m != -1) {

                    val slot = slots[m]
                    if (slot.index < i) {

                        var ii = i
                        var jj = slot.index

                        repeat(block.size) {

                            disk[jj] = disk[ii]
                            disk[ii] = Free

                            jj++
                            ii--
                        }

                        val freeSize = (slot.size - block.size)
                        if (freeSize == 0) {

                            slots.removeAt(m)
                            slots += Slot(index = i, size = block.size)

                        } else {

                            val z = slot.index + block.size
                            slots[m] = Slot(index = z, size = freeSize)
                        }
                    }
                }
            }
        }

        disk.forEachIndexed { index, block ->

            if (block is File) {

                checksum += (index * block.id)
            }
        }

        return checksum
    }
}

sealed interface Block {

    data class File(val id: Int, val size: Int) : Block

    data object Free : Block
}

private fun String.toBlocks(): List<Block> {

    val result = LinkedList<Block>()

    var identifier = 0
    forEachIndexed { index, digit ->

        val size = Character.digit(digit, 10)
        if (index % 2 == 0) {

            repeat(size) { result += File(id = identifier, size = size) }
            identifier++

        } else {

            repeat(size) { result += Free }
        }
    }

    return result
}

private fun Array<Block>.print() {

    joinToString(separator = "") { block ->

        when (block) {

            is File -> "${block.id}"
            is Free -> "."
        }

    }.println()
}