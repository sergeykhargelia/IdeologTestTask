@file: JvmName("main")

import java.io.FileNotFoundException
import java.io.File
import java.io.PrintWriter

const val threadNamePosition = 5
const val mostFrequentCount = 10

/**
 * This function takes the log as Sequence<String>
 * and returns a list of pairs, representing most frequent threads.
 * Each pair consists of the thread name and the number of lines in the log
 * with this thread
 */

fun getMostFrequentThreads(data: Sequence<String>): List<Pair<String, Int>> {
    val logsByThread = data.groupBy {
        it.split(Regex("\\s+")).getOrElse(threadNamePosition) {
            throw IllegalArgumentException("Wrong structure of log file")
        }
    }

    return logsByThread.toList().map { (thread, logs) ->
        Pair (thread, logs.size)
    }.sortedByDescending { (_, count) -> count }.take(mostFrequentCount)
}

fun main(args: Array<String>) {
    try {
        if (args.isEmpty()) {
            throw IllegalArgumentException("There is no input file name in the program arguments.")
        }
        val inputFileName = args[0]
        if (!File(inputFileName).exists()) {
            throw FileNotFoundException(inputFileName)
        }

        File(inputFileName).useLines { data ->
            val mostFrequentThreads = getMostFrequentThreads(data)

            if (args.size >= 2) {
                val outputFileName = args[1]
                val writer = PrintWriter(outputFileName)
                mostFrequentThreads.forEach { (threadName, actionsCount) ->
                    writer.append("$threadName $actionsCount\n")
                }
                writer.close()
            } else {
                mostFrequentThreads.forEach { (threadName, actionsCount) ->
                    println("$threadName $actionsCount")
                }
            }
        }
    } catch (e: IllegalArgumentException) {
        println(e.toString())
    } catch (e: FileNotFoundException) {
        println("Cannot find the file $e")
    }
}