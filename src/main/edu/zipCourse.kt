package edu

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun main(args: Array<String>) {
    zipCourse("edu/course.json", "edu/course.zip")
}

fun zipCourse(courseJson: String, courseZip: String) {
    val fileToZip = File(courseJson)
    val fis = FileInputStream(fileToZip)
    val zipEntry = ZipEntry(fileToZip.name)

    val fos = FileOutputStream(courseZip)
    val zipOut = ZipOutputStream(fos)
    zipOut.putNextEntry(zipEntry)
    val bytes = ByteArray(1024)
    var length = fis.read(bytes)
    while (length >= 0) {
        zipOut.write(bytes, 0, length)
        length = fis.read(bytes)
    }
    zipOut.close()
    fis.close()
    fos.close()
}