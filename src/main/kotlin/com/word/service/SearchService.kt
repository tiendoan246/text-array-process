package com.word.service

import com.word.exception.ArgumentEmptyException
import com.word.domain.SearchType
import com.word.exception.InvalidArgumentException
import com.word.exception.OperationIsNotSupportedException

interface SearchService {
    @Throws(InvalidArgumentException::class, ArgumentEmptyException::class, OperationIsNotSupportedException::class)
    fun search(words: List<String>, inputConditions: List<String>, settings: Map<String, SearchType>): List<String>
}