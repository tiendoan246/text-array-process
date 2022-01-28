package com.word.constant

import java.util.stream.Stream

enum class Action(private val code: String) {
    OPTION("OPTION"),
    MAX("MAX"),
    MIN("MIN"),
    START("START"),
    END("END"),
    CONTAINS("CONTAINS");

    companion object {
        @JvmStatic
        fun from(code: String): Action {
            return Stream.of(*values()).filter { targetEnum: Action -> targetEnum.code == code }.findFirst().orElse(null)
        }
    }
}