package com.lumera.service

import com.lumera.exception.ArgumentEmptyException
import com.lumera.domain.SearchType
import com.lumera.exception.InvalidArgumentException
import com.lumera.exception.OperationIsNotSupportedException

interface SearchService {
    @Throws(InvalidArgumentException::class, ArgumentEmptyException::class, OperationIsNotSupportedException::class)
    fun search(words: List<String>, inputConditions: List<String>, settings: Map<String, SearchType>): List<String>
}