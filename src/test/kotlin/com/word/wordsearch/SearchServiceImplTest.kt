package com.word.wordsearch

import com.word.constant.Action
import com.word.domain.SearchType
import com.word.exception.ArgumentEmptyException
import com.word.exception.InvalidArgumentException
import com.word.exception.OperationIsNotSupportedException
import com.word.service.SearchService
import com.word.service.SearchServiceImpl
import org.junit.Assert
import org.junit.Test
import java.util.*

class SearchServiceImplTest {

    private var inputConditions: List<String> = ArrayList()
    private val searchService: SearchService = SearchServiceImpl()

    @Test(expected = ArgumentEmptyException::class)
    @Throws(InvalidArgumentException::class, ArgumentEmptyException::class, OperationIsNotSupportedException::class)
    fun test_search_whenArgumentEmpty_shouldThrowArgumentEmptyException() {
        searchService.search(words, ArrayList(), settings)
    }

    @Test(expected = InvalidArgumentException::class)
    @Throws(InvalidArgumentException::class, ArgumentEmptyException::class, OperationIsNotSupportedException::class)
    fun test_search_whenArgumentIsInvalid_shouldThrowInvalidArgumentException() {
        inputConditions = ArrayList(listOf(
                "class=palindrome",
                "maxlength"
        ))
        searchService.search(words, inputConditions, settings)
    }

    @Test(expected = InvalidArgumentException::class)
    @Throws(InvalidArgumentException::class, ArgumentEmptyException::class, OperationIsNotSupportedException::class)
    fun test_search_whenArgumentIsNotDefined_shouldThrowInvalidArgumentException() {
        inputConditions = ArrayList(listOf(
                "class=palindrome",
                "maxlength=8",
                "word=hello"
        ))
        searchService.search(words, inputConditions, settings)
    }

    @Test
    @Throws(InvalidArgumentException::class, ArgumentEmptyException::class, OperationIsNotSupportedException::class)
    fun test_search_whenInputValidMaxLength_shouldReturnSearchResult() {
        inputConditions = ArrayList(listOf(
                "maxlength=3"
        ))
        val result = searchService.search(words, inputConditions, settings)
        Assert.assertNotNull(result)
        Assert.assertEquals(3, result.size.toLong())
    }

    @Test
    @Throws(InvalidArgumentException::class, ArgumentEmptyException::class, OperationIsNotSupportedException::class)
    fun test_search_whenInputValidMaxLengthAndClassPalindrome_shouldReturnSearchResult() {
        inputConditions = ArrayList(listOf(
                "class=palindrome",
                "maxlength=3"
        ))
        val result = searchService.search(words, inputConditions, settings)
        Assert.assertNotNull(result)
        Assert.assertEquals(1, result.size.toLong())
        Assert.assertEquals("cac", result.stream().findFirst().get())
    }

    @Test
    @Throws(InvalidArgumentException::class, ArgumentEmptyException::class, OperationIsNotSupportedException::class)
    fun test_search_whenInputValidMaxLengthAndClassIsogram_shouldReturnSearchResult() {
        inputConditions = ArrayList(listOf(
                "class=isogram",
                "maxlength=8"
        ))
        val result = searchService.search(words, inputConditions, settings)
        Assert.assertNotNull(result)
        Assert.assertEquals(1, result.size.toLong())
        Assert.assertEquals("spring", result.stream().findFirst().get())
    }

    @Test
    @Throws(InvalidArgumentException::class, ArgumentEmptyException::class, OperationIsNotSupportedException::class)
    fun test_search_whenInputValidMaxLengthAndMinLength_shouldReturnSearchResult() {
        inputConditions = ArrayList(listOf(
                "maxlength=3",
                "minlength=3"
        ))
        val result = searchService.search(words, inputConditions, settings)
        Assert.assertNotNull(result)
        Assert.assertEquals(3, result.size.toLong())
    }

    @Test
    @Throws(InvalidArgumentException::class, ArgumentEmptyException::class, OperationIsNotSupportedException::class)
    fun test_search_whenInputValidStartWiths_shouldReturnSearchResult() {
        inputConditions = ArrayList(listOf(
                "startswith=s"
        ))
        val result = searchService.search(words, inputConditions, settings)
        Assert.assertNotNull(result)
        Assert.assertEquals(3, result.size.toLong())
    }

    @Test
    @Throws(InvalidArgumentException::class, ArgumentEmptyException::class, OperationIsNotSupportedException::class)
    fun test_search_whenInputValidEndWiths_shouldReturnSearchResult() {
        inputConditions = ArrayList(listOf(
                "endswith=a"
        ))
        val result = searchService.search(words, inputConditions, settings)
        Assert.assertNotNull(result)
        Assert.assertEquals(2, result.size.toLong())
    }

    @Test
    @Throws(InvalidArgumentException::class, ArgumentEmptyException::class, OperationIsNotSupportedException::class)
    fun test_search_whenInputValidContains_shouldReturnSearchResult() {
        inputConditions = ArrayList(listOf(
                "containsonly=ac"
        ))
        val result = searchService.search(words, inputConditions, settings)
        Assert.assertNotNull(result)
        Assert.assertEquals(9, result.size.toLong())
    }

    @Test
    @Throws(InvalidArgumentException::class, ArgumentEmptyException::class, OperationIsNotSupportedException::class)
    fun test_search_whenInputValidMaxLengthAndSemordnilap_shouldReturnSearchResult() {
        inputConditions = ArrayList(listOf(
                "maxlength=3",
                "class=semordnilap"
        ))
        val result = searchService.search(words, inputConditions, settings)
        Assert.assertNotNull(result)
        Assert.assertEquals(1, result.size.toLong())
    }

    companion object {
        private var words: List<String> = ArrayList(listOf(
                "stats",
                "succus",
                "aasvogels",
                "aah",
                "haa",
                "cac",
                "banana",
                "decade",
                "welcome",
                "spring"
        ))
        private var settings: Map<String, SearchType> = HashMap(mapOf(
                "class" to SearchType("{isogram|palindrome|semordnilap}", Action.from("OPTION")),
                "maxlength" to SearchType("<INT>", Action.from("MAX")),
                "minlength" to SearchType("<INT>", Action.from("MIN")),
                "startswith" to SearchType("<STRING>", Action.from("START")),
                "endswith" to SearchType("<STRING>", Action.from("END")),
                "containsonly" to SearchType("<STRING>", Action.from("CONTAINS"))
        ))
    }
}