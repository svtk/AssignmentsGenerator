package edu

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.io.OutputStream

fun writeJson(any: Any, jsonFile: File) {
    val mapper = ObjectMapper()
    mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, any)
}

fun writeJson(any: Any, outputStream: OutputStream) {
    val mapper = ObjectMapper()
    mapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, any)
}