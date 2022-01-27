package com.lumera.wordsearch

import com.lumera.domain.SearchType
import com.lumera.exception.ArgumentEmptyException
import com.lumera.exception.InvalidArgumentException
import com.lumera.exception.OperationIsNotSupportedException
import com.lumera.service.FileService
import com.lumera.service.SearchService
import com.lumera.service.SearchServiceImpl
import java.io.IOException
import java.util.*
import java.util.function.Consumer
import kotlin.system.exitProcess

class App {
    private val wordFilePath = "wordlist.txt"
    private val conditionFilePath = "conditions.txt"

    // List of words load from file
    private lateinit var words: List<String>
    private var conditionSettings: Map<String, SearchType> = HashMap()
    private val searchService: SearchService = SearchServiceImpl()

    fun run(args: Array<String>): Int {
        // rules should be parsed from args
        if (args.isEmpty()) {
            println("Please input your search options")
            return 1
        }
        try {
            val result = searchService.search(words, listOf(*args), conditionSettings)
            if (!result?.isEmpty()!!) {
                println(String.format("Found strings (%s)", result.stream().count()))
                result.forEach(Consumer { s: String -> println("item: $s") })
            } else {
                println("Not found any strings match your conditions")
            }
        } catch (e: InvalidArgumentException) {
            println("An exception occurs with invalid arguments: " + e.message)
            e.printStackTrace()
        } catch (e: ArgumentEmptyException) {
            println("An exception occurs with empty arguments: " + e.message)
            e.printStackTrace()
        } catch (e: OperationIsNotSupportedException) {
            println("An exception occurs with search options is not supported: " + e.message)
            e.printStackTrace()
        } catch (e: Exception) {
            println("An error occurs: " + e.message)
            e.printStackTrace()
        }
        return 0
    }

    private fun initWords() {
        try {
            words = FileService.instance.readFile(wordFilePath)

            if (words.stream().count() > 0) {
                Collections.sort(words)
            }
        } catch (e: IOException) {
            println(String.format("Error occurs while reading file: %s", wordFilePath))
            e.printStackTrace()
        } catch (e: Exception) {
            println("An error occurs: " + e.message)
            e.printStackTrace()
        }
    }

    private fun initConditions() {
        try {
            conditionSettings = FileService.instance.readFileSettings(conditionFilePath)
        } catch (e: IOException) {
            println(String.format("Error occurs while reading file: %s", conditionFilePath))
            e.printStackTrace()
        } catch (e: Exception) {
            println("An error occurs: " + e.message)
            e.printStackTrace()
        }
    }

    init {
        // Init words from text file
        initWords()

        // Init search conditions
        initConditions()
    }
}

fun main(args: Array<String>) {
    exitProcess(App().run(args))
}
