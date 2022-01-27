package com.lumera.exception

class OperationIsNotSupportedException : Exception {
    var itemError: String? = null
        private set

    constructor() : super() {}

    constructor(message: String?, itemError: String?) : super(String.format(message!!, itemError)) {
        this.itemError = itemError
    }
}