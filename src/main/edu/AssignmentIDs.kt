package edu

import java.io.FileInputStream
import java.util.*

object AssignmentIDs {
    val propertiesFile = "/Users/svtk/Coursera/KotlinCourseraAssignments/edu/assignmentIDs.properties"

    private val properties by lazy {
        Properties().also { properties ->
            FileInputStream(propertiesFile).use {
                properties.load(it)
            }
        }
    }

    private fun prefix(assignmentInfo: AssignmentInfo) =
            "week${assignmentInfo.weekInfo.weekNumber}_${assignmentInfo.packageName}_"

    fun getPartId(assignmentInfo: AssignmentInfo): String =
            properties.getProperty(prefix(assignmentInfo) + "partId")

    fun getAssignmentKey(assignmentInfo: AssignmentInfo): String =
            properties.getProperty(prefix(assignmentInfo) + "assignmentKey")
}