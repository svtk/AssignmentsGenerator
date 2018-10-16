package edu

import java.io.File

data class WeekInfo(
        val weekNumber: Int,
        val assignments: List<AssignmentInfo>
) {
    init {
        for (assignment in assignments) {
            assignment.weekInfo = this
        }
    }
    private val courseTitle = "Kotlin for Java Developers"
    val courseSummary = "Assignment for Week $weekNumber of the Coursera " +
            "<a href=\"https://www.coursera.org/learn/kotlin-for-java-developers/\">Kotlin for Java Developers</a> course"

    val title get() = "$courseTitle. Week $weekNumber"
    val dirName get() = "week$weekNumber"
}

data class AssignmentInfo(
        val title: String,
        val packageName: String,
        val markdownFile: File,
        val sourceFiles: List<TaskFileInfo>,
        val testFiles: List<TaskFileInfo>
) {
    lateinit var weekInfo: WeekInfo
    override fun toString(): String {
        return "AssignmentInfo(title='$title', packageName='$packageName',\n  markdownFile=$markdownFile,\n  sourceFiles=${sourceFiles.joinToString("\n     ")},\n  testFiles=${testFiles.joinToString("\n    ")})\n\n"
    }
}

data class TaskFileInfo(private val rootDir: String, private val path: String, val sampleInfo: SampleInfo) {
    val fileName: String = path.substringAfterLast("/")
    val fullPath: String = "$rootDir/$path"
    val shortPath: String = "$rootDir/$fileName"
}

private val ASSIGNMENTS_PROJECT = "/Users/svtk/Coursera/KotlinCourseraAssignments/src"
private val SOURCES_PATH = "$ASSIGNMENTS_PROJECT/main"
private val TESTS_PATH = "$ASSIGNMENTS_PROJECT/test/initial"

fun createAssignmentInfo(
        title: String,
        week: String,
        packageName: String,
        additionalSourceFiles: List<TaskFileInfo> = emptyList()
): AssignmentInfo {
    val sourcesDir = File("$SOURCES_PATH/$week/$packageName")
    val taskDescription = sourcesDir.subFile("task.md")

    val sourceFiles = getSourceFiles(week, packageName)

    val testFiles = getTestFiles(week, packageName)
    return AssignmentInfo(title, packageName, taskDescription,
            additionalSourceFiles + sourceFiles, testFiles)
}

fun getSourceFiles(week: String, packageName: String): List<TaskFileInfo> {
    val sourcesDir = File("$SOURCES_PATH/$week/$packageName")
    return sourcesDir
            .walk()
            .filter { it.extension == "kt" }
            .map { TaskFileInfo("src", it.path.substringAfter("$SOURCES_PATH/$week/"), SampleInfo(it)) }
            .toList()
}

fun getTestFiles(week: String, packageName: String): List<TaskFileInfo> {
    val testsDir = File("$TESTS_PATH/$week/$packageName")
    return testsDir
            .walk()
            .filter { it.extension == "kt" }
            .map { TaskFileInfo("test", it.path.substringAfter("$TESTS_PATH/$week/"), SampleInfo(it)) }
            .toList()
}
