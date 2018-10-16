package edu

import java.io.FileInputStream
import java.util.*

object CourseProperties {
    private val assignmentIDsFile = "/Users/svtk/Coursera/KotlinCourseraAssignments/edu/assignmentIDs.properties"
    private val coursePropertiesFile = "/Users/svtk/Coursera/KotlinCourseraAssignments/edu/course.properties"

    private val properties by lazy {
        Properties().also { properties ->
            for (file in listOf(assignmentIDsFile, coursePropertiesFile)) {
                FileInputStream(file).use {
                    properties.load(it)
                }
            }
        }
    }

    private fun prefix(assignmentInfo: AssignmentInfo) =
            "week${assignmentInfo.weekInfo.weekNumber}_${assignmentInfo.packageName}_"

    fun getPartId(assignmentInfo: AssignmentInfo): String =
            properties.getProperty(prefix(assignmentInfo) + "partId")

    fun getAssignmentKey(assignmentInfo: AssignmentInfo): String =
            properties.getProperty(prefix(assignmentInfo) + "assignmentKey")

    fun getFeedbackLink(assignmentInfo: AssignmentInfo): String =
            "https://www.coursera.org/learn/kotlin-for-java-developers/programming/" +
                    properties.getProperty(prefix(assignmentInfo) + "linkKey") + "/discussions"

    fun getFormatVersion(): Int {
        val propertyName = "course_format_version"
        val formatVersion = properties.getProperty(propertyName)
                ?: throw AssertionError("No '$propertyName' property found in $coursePropertiesFile")
        return formatVersion.toIntOrNull()
                ?: throw AssertionError("Property '$propertyName' should be integer")
    }
}