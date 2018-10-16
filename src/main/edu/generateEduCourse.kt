package edu

import java.io.File

fun main(args: Array<String>) {
    val mastermind = createAssignmentInfo("Mastermind", "_1week", "mastermind")
    val niceString = createAssignmentInfo("Nice String", "_2week", "nicestring")
    val taxiPark = createAssignmentInfo("Taxi Park", "_2week", "taxipark")
    val rationals = createAssignmentInfo("Rationals", "_3week", "rationals")
    val board = createAssignmentInfo("Board", "_3week", "board")
    val games = createAssignmentInfo("Games", "_4week", "games",
            getSourceFiles("_3week", "board"))

    val week2 = WeekInfo(2, listOf(mastermind))
    val week3 = WeekInfo(3, listOf(niceString, taxiPark))
    val week4 = WeekInfo(4, listOf(rationals, board))
    val week5 = WeekInfo(5, listOf(games))

    generateEduCourse(week2)
    generateEduCourse(week3)
    generateEduCourse(week4)
    generateEduCourse(week5)
}

private fun generateEduCourse(weekInfo: WeekInfo) {
    val course = Course(
            summary = weekInfo.courseSummary,
            title = weekInfo.title,
            programming_language = "kotlin",
            language = "en",
            items = weekInfo.generateLessons() + getLessonWithBuildFile(),
            version = AssignmentIDs.getFormatVersion())

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
            sourceFiles.map { it.fullPath to TaskFile(it.fullPath, it.sampleInfo.taskText, it.sampleInfo.placeholders) }.toMap()

    val testsAsFiles =
//            testFiles.map { it.fullPath to TaskFile(it.fullPath, it.sampleInfo.code, listOf()) }.toMap()
            testFiles.map { it.shortPath to TaskFile(it.shortPath, it.sampleInfo.code, listOf()) }.toMap()

    val testsAsTests =
            testFiles.map { it.shortPath to it.sampleInfo.code }.toMap()

    val additionalFiles = mapOf(
            "partId" to AdditionalFile(AssignmentIDs.getPartId(this), visible = false),
            "assignmentKey" to AdditionalFile(AssignmentIDs.getAssignmentKey(this), visible = false))

    val feedbackLink = FeedbackLink("CUSTOM", AssignmentIDs.getFeedbackLink(this))

    return Lesson(0, title,
            0,
            listOf(Task("Task", 0,
                    description_text = descriptionText,
                    description_format = "md",
                    task_files = taskFiles + testsAsFiles,
                    test_files = testsAsTests,
                    additional_files = additionalFiles,
                    feedback_link = feedbackLink)))
}

fun getLessonWithBuildFile(): Lesson {
    val fileName = "build.gradle"
    val buildFileText = File("edu/build.gradle.txt").readText()
    val additionalFiles = mapOf(fileName to
            AdditionalFile(buildFileText, visible = false))
    val name = "Edu additional materials"
    val task = Task(name, 0,
            additional_files = additionalFiles,
            task_files = emptyMap(),
            test_files = emptyMap(),
            feedback_link = FeedbackLink(link_type = "STEPIK")
    )
    return Lesson(0, name, 0, listOf(task))
}