package com.word.service

import com.word.constant.Action
import com.word.constant.ExceptionConstants
import com.word.domain.SearchType
import com.word.exception.ArgumentEmptyException
import com.word.exception.InvalidArgumentException
import com.word.exception.OperationIsNotSupportedException
import java.util.function.Predicate
import java.util.stream.Collectors

class SearchServiceImpl : SearchService {

    @Throws(InvalidArgumentException::class, ArgumentEmptyException::class, OperationIsNotSupportedException::class)
    override fun search(words: List<String>, inputConditions: List<String>, settings: Map<String, SearchType>): List<String> {

        // Validate user input arguments
        validateConditions(inputConditions, settings)

        val filters = getPredicates(inputConditions, settings)

        var result = words
                .stream()
                .filter(
                        filters.stream()
                                .reduce { obj: Predicate<String>, other: Predicate<in String>? -> obj.and(other) }
                                .orElse(Predicate { false }))
                .collect(Collectors.toList())

        // Apply semordnilap after other conditions just applied
        if (inputConditions.contains("class=semordnilap")) {
            result = applySemordnilapCondition(result, words)
        }

        return result
    }

    private fun applySemordnilapCondition(result: List<String>, words: List<String>): List<String> {
        val applySemordnilap: MutableList<String> = ArrayList()
        if (result.isNotEmpty()) {
            var semordnilapItem: String? = null
            for (word in result) {
                semordnilapItem = binarySearch(words, StringUtil.reverseString(word))
                if (semordnilapItem != null) {
                    applySemordnilap.add(semordnilapItem)
                }
            }
            return applySemordnilap
        }
        return result
    }

    private fun binarySearch(array: List<String>, str: String): String? {
        var low = 0
        var high = array.size - 1
        var mid: Int
        while (low <= high) {
            mid = (low + high) / 2
            if (array[mid].compareTo(str) < 0) {
                low = mid + 1
            } else if (array[mid].compareTo(str) > 0) {
                high = mid - 1
            } else {
                // array[mid] is found item
                return str;
            }
        }
        return null
    }

    @Throws(OperationIsNotSupportedException::class)
    private fun getPredicates(inputConditions: List<String>, settings: Map<String, SearchType>): Collection<Predicate<String>> {
        val predicates: MutableList<Predicate<String>> = ArrayList()
        var name: String? = null
        var arr: Array<String>
        var searchType: SearchType?

        for (condition in inputConditions) {
            // maxlength=3
            arr = condition.split("=".toRegex()).toTypedArray()
            name = arr[0] // maxlength
            searchType = settings[name] // SearchType (3, MAX)
            var predicate: Predicate<String>

            when (searchType!!.operation) { // MAX
                Action.OPTION -> {
                    val specialCondition = arr[1]
                    // class={isogram|palindrome|semordnilap}
                    predicate = Predicate { conditionItem: String -> checkWordSpecialCondition(conditionItem, specialCondition) }
                    predicates.add(predicate)
                }
                Action.MAX -> {
                    val max = arr[1]
                    predicate = Predicate { maxItem: String -> maxItem.length <= max.toInt() }
                    predicates.add(predicate)
                }
                Action.MIN -> {
                    val min = arr[1]
                    predicate = Predicate { minItem: String -> minItem.length >= min.toInt() }
                    predicates.add(predicate)
                }
                Action.START -> {
                    val start = arr[1]
                    predicate = Predicate { startItem: String -> startItem.startsWith(start) }
                    predicates.add(predicate)
                }
                Action.END -> {
                    val end = arr[1]
                    predicate = Predicate { endItem: String -> endItem.endsWith(end) }
                    predicates.add(predicate)
                }
                Action.CONTAINS -> {
                    val characters = arr[1]
                    predicate = Predicate { containCharItem: String -> StringUtil.containCharArray(containCharItem, characters.toCharArray()) }
                    predicates.add(predicate)
                }
                else -> throw OperationIsNotSupportedException(ExceptionConstants.OPERATION_IS_NOT_SUPPORTED_ERROR_MESSAGE, searchType.operation.toString())
            }
        }
        return predicates
    }

    private fun checkWordSpecialCondition(value: String, condition: String): Boolean {
        when (condition) {
            "isogram" -> return StringUtil.isContainDuplicatedCharacter(value)
            "palindrome" -> return StringUtil.isPalindrome(value)
            "semordnilap" -> return isSemordnilap(value)
        }
        return false
    }

    //Just return true to skip this now, make search better performance
    private fun isSemordnilap(value: String): Boolean {
        return true
    }

    @Throws(ArgumentEmptyException::class, InvalidArgumentException::class)
    private fun validateConditions(inputConditions: List<String>?, settings: Map<String, SearchType>) {
        if (inputConditions == null || inputConditions.isEmpty()) {
            throw ArgumentEmptyException(ExceptionConstants.ARGUMENT_REQUIRED_ERROR_MESSAGE, java.lang.String.join(",", settings.keys))
        }

        var arr: Array<String>

        // maxlength=3
        for (condition in inputConditions) {
            arr = condition.split("=".toRegex()).toTypedArray()

            // Invalid argument format
            if (arr.size != 2) {
                throw InvalidArgumentException(ExceptionConstants.INVALID_ARGUMENT_ERROR_MESSAGE, condition)
            }

            if (!settings.keys.contains(arr[0])) {
                throw InvalidArgumentException(ExceptionConstants.INVALID_ARGUMENT_ERROR_MESSAGE, condition)
            }
        }
    }
}