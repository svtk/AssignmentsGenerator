package edu

import java.io.File

fun main(args: Array<String>) {
    val mastermind = createAssignmentInfo("Mastermind", "_1week", "mastermind")
    val niceString = createAssignmentInfo("Nice String", "_2week", "nicestring")
    val taxiPark = createAssignmentInfo("Taxi Park", "_2week", "taxipark")
    val rationals = createAssignmentInfo("Rationals", "_3week", "rationals")
    val board = createAssignmentInfo("Board", "_3week", "board")
    val games = createAssignmentInfo("Games", "_4week", "games")

    val week2 = WeekInfo(2, listOf(mastermind))
    val week3 = WeekInfo(3, listOf(niceString, taxiPark))
    val week4 = WeekInfo(4, listOf(rationals, board))
    val week5 = WeekInfo(5, listOf(games))

    generateEduCourse(week2)
}

private fun generateEduCourse(weekInfo: WeekInfo) {
    val course = Course(
            summary = weekInfo.courseSummary,
            title = weekInfo.title,
            programming_language = "kotlin",
            language = "en",
            items = weekInfo.generateLessons())

    File("edu").mkdir()
    File("edu/${weekInfo.dirName}").mkdir()
    val jsonFile = "edu/${weekInfo.dirName}/course.json"
    val zipFile = "edu/${weekInfo.dirName}/${weekInfo.dirName}.zip"

    writeJson(course, File(jsonFile))
    println("$jsonFile updated")
    zipCourse(jsonFile, zipFile)
    println("$zipFile generated")
}

fun WeekInfo.generateLessons(): List<Lesson> {
    return assignments.map { it.generateLesson() }
}

fun AssignmentInfo.generateLesson(): Lesson {
    val descriptionText = markdownFile.readText()

    val taskFiles =
            sourceFiles.map { it.path to TaskFile(it.path, it.sampleInfo.taskText, it.sampleInfo.placeholders ) }.toMap()

    val testsAsFiles =
            testFiles.map { it.path to TaskFile(it.path, it.sampleInfo.code, listOf()) }.toMap()

    val testsAsTests =
            testFiles.map { "test/Tests.kt" to it.sampleInfo.code }.toMap()

    val additionalFiles = mapOf(
            "partId" to AdditionalFile(AssignmentIDs.getPartId(this), visible = false),
            "assignmentKey" to AdditionalFile(AssignmentIDs.getAssignmentKey(this), visible = false))

    return Lesson(0, title,
            0,
            listOf(Task(title, 0,
                    description_text = descriptionText,
                    description_format = "md",
                    task_files = taskFiles + testsAsFiles,
                    test_files = testsAsTests,
                    additional_files = additionalFiles)))
}