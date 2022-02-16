@file: JvmName("main")

import java.io.FileNotFoundException
import java.io.File
import java.io.PrintWriter

const val threadNamePosition = 5
const val mostFrequentCount = 10

fun main(args: Array<String>) {
    try {
        if (args.isEmpty()) {
            throw IllegalArgumentException("There is no input file name in the program arguments.")
        }
        val inputFileName = args[0]
        if (!File(inputFileName).exists()) {
            throw FileNotFoundException(inputFileName)
        }
        val data = File(inputFileName).readLines()


        val logsByThread = data.groupBy {
            it.split(Regex("\\s+")).getOrElse(threadNamePosition) {
                throw IllegalArgumentException("Wrong structure of log file")
            }
        }
        val mostFrequentThreads = logsByThread.toList().map { (thread, logs) ->
            Pair (thread, logs.size)
        }.sortedByDescending { (_, count) -> count }.take(mostFrequentCount)


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
    } catch (e: IllegalArgumentException) {
        println(e.toString())
    } catch (e: FileNotFoundException) {
        println("Cannot find a file $e")
    }
}