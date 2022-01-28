package com.word.service

import com.word.constant.Action
import com.word.constant.Action.Companion.from
import com.word.domain.SearchType
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

class FileService private constructor() {
    @Throws(IOException::class)
    fun readFile(filePath: String): List<String> {

        val list: MutableList<String> = ArrayList()
        var line: String? = null

        BufferedReader(FileReader(filePath)).use { reader ->
            while (reader.readLine().also { line = it } != null) {
                list.add(line as String)
            }
        }
        return list
    }

    @Throws(IOException::class)
    fun readFileSettings(filePath: String?): Map<String, SearchType> {
        val map: MutableMap<String, SearchType> = HashMap()
        var line: String? = null
        var arr: Array<String>
        var type: Array<String?>
        var searchType: SearchType? = null

        BufferedReader(FileReader(filePath)).use { reader ->
            while (reader.readLine().also { line = it } != null) {
                // Skip the comment line
                if (line!!.startsWith("#")) {
                    continue
                }
                arr = line!!.split("=").toTypedArray()

                // Skip invalid line
                if (arr.size != 2) {
                    continue
                }

                // Add search type settings
                type = arr[1].split(":").toTypedArray()
                // If search action is not specific, ad default
                searchType = SearchType(type[0], if (type.size == 1) Action.CONTAINS else from(type[1]!!))
                map[arr[0]] = searchType!!
            }
        }
        return map
    }

    companion object {
        private var fileService: FileService? = null
        val instance: FileService
            get() {
                if (fileService == null) {
                    fileService = FileService()
                }
                return fileService as FileService
            }
    }
}