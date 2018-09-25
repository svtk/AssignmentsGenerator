package edu

import java.io.File

fun File.subFile(name: String) = File("$path/$name")