package edu

import java.io.File

private val TODO_START = "/*<todo>*/"
private val TODO_END = "/*</todo>*/"
private val TODO_TEXT = "TODO()"

private val SKIP_START = "/*<skip>*/"
private val SKIP_END = "/*</skip>*/"

sealed class Snippet
data class CodeSnippet(val code: String) : Snippet()
data class TaskSnippet(val placeholder: String) : Snippet()

private fun createTaskSnippet(code: String): TaskSnippet {
    val placeholder = if (code.startsWith(SKIP_START)) {
        ""
    } else {
        TODO_TEXT
    }

    return TaskSnippet(placeholder)
}

class SampleInfo(val file: File) {
    val code = file.readText()

    val snippets: List<Snippet> by lazy {
        code.split(TODO_END, SKIP_END).flatMap {
            when {
                it.contains(TODO_START) -> listOf(CodeSnippet(it.substringBefore(TODO_START)),
                        createTaskSnippet(it.substringAfter(TODO_START)))
                it.contains(SKIP_START) -> listOf(CodeSnippet(it.substringBefore(SKIP_START)),
                        createTaskSnippet(it.substringAfter(SKIP_START)))
                else -> listOf(CodeSnippet(it))
            }
        }
    }

    val taskText by lazy {
        snippets.joinToString("") {
            when (it) {
                is CodeSnippet -> it.code
                is TaskSnippet -> it.placeholder
            }
        }
    }

    val taskSnippets by lazy { snippets.filterIsInstance<TaskSnippet>() }

    val offsets: List<Int> by lazy {
        val offsets = arrayListOf<Int>()
        var currentLength = 0
        snippets.forEach {
            when (it) {
                is CodeSnippet -> {
                    currentLength += it.code.length
                }
                is TaskSnippet -> {
                    offsets += currentLength
                    currentLength += it.placeholder.length
                }
            }
        }
        offsets
    }

    val placeholders: List<Placeholder>
        get() = taskSnippets.zip(offsets).map { (taskSnippet, offset) ->
            Placeholder(offset, taskSnippet.placeholder.length,
                    listOf(), taskSnippet.placeholder)
        }
}
