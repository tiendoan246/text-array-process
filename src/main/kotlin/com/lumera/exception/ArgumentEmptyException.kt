package com.lumera.exception

class ArgumentEmptyException : Exception {
    val itemError: String? = null

    constructor() : super() {}

    constructor(message: String?, itemError: String?) : super(String.format(message!!, itemError)) {}
}