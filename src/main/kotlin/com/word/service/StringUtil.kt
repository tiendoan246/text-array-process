package com.word.service

class StringUtil {
    companion object {
        @JvmStatic
        fun isPalindrome(value: String): Boolean {
            var i = 0
            var j = value.length - 1

            while (i < j) {
                if (value[i] != value[j]) {
                    return false
                }
                i++
                j--
            }
            return true
        }

        fun reverseString(str: String): String {
            val sb = StringBuilder(str)
            sb.reverse()
            return sb.toString()
        }

        fun isContainDuplicatedCharacter(value: String): Boolean {
            val charMap: MutableMap<Any, Any> = HashMap()

            for (c in value.toCharArray()) {
                if (charMap.containsKey(c)) {
                    return false
                }
                charMap[c] = c
            }
            return true
        }

        fun containCharArray(value: String, arr: CharArray): Boolean {
            for (c in arr) {
                if (value.indexOf(c) != -1) {
                    return true
                }
            }
            return false
        }
    }
}