package edu

import java.io.File

data class WeekInfo(
        val weekNumber: Int,
        val assignments: List<AssignmentInfo>
) {
    private val courseTitle = "Kotlin for Java Developers."
    val courseSummary = "Assignment for Week $weekNumber of the Coursera course 'Kotlin for Java Developers'"
    val title get() = "$courseTitle Week $weekNumber."
    val dirName get() = "week$weekNumber"
}

data class AssignmentInfo(
        val title: String,
        val packageName: String,
        val markdownFile: File,
        val sourceFiles: List<TaskFileInfo>,
        val testFiles: List<TaskFileInfo>
) {
    override fun toString(): String {
        return "AssignmentInfo(title='$title', packageName='$packageName',\n  markdownFile=$markdownFile,\n  sourceFiles=${sourceFiles.joinToString("\n     ")},\n  testFiles=${testFiles.joinToString("\n    ")})\n\n"
    }
}

data class TaskFileInfo(val path: String, val sampleInfo: SampleInfo)

private val ASSIGNMENTS_PROJECT = "/Users/svtk/Coursera/KotlinCourseraAssignments/src"
private val SOURCES_PATH = "$ASSIGNMENTS_PROJECT/main"
private val TESTS_PATH = "$ASSIGNMENTS_PROJECT/test/initial"

fun createAssignmentInfo(title: String, week: String, packageName: String): AssignmentInfo {
    val sourcesDir = File("$SOURCES_PATH/$week/$packageName")
    val taskDescription = sourcesDir.subFile("task.md")

    val sourceFiles = sourcesDir
            .walk()
            .filter { it.extension == "kt" }
            .map { TaskFileInfo("src/" + it.path.substringAfter("$SOURCES_PATH/$week/"), SampleInfo(it)) }
            .toList()

    val testsDir = File("$TESTS_PATH/$week/$packageName")
    val testFiles = testsDir
            .walk()
            .filter { it.extension == "kt" }
            .map { TaskFileInfo("test/" + it.path.substringAfter("$TESTS_PATH/$week/"), SampleInfo(it)) }
            .toList()
    return AssignmentInfo(title, packageName, taskDescription, sourceFiles, testFiles)
}