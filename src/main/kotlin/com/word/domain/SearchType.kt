package com.word.domain

import com.word.constant.Action

class SearchType {
    var dataType: String? = null
    var operation: Action? = null

    constructor() {}

    constructor(dataType: String?, operation: Action?) {
        this.dataType = dataType
        this.operation = operation
    }
}