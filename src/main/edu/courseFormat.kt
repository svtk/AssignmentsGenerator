package edu

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class Course(
        @field:JsonInclude(JsonInclude.Include.NON_NULL)
        val id: Int? = null,
        val summary: String,
        val title: String,
        val programming_language: String,
        val language: String,
        val items: List<Lesson>,
        val version: Int
)

data class Lesson(
        val id: Int,
        val title: String,
        val unit_id: Int,
        val task_list: List<Task>,
        val type: String = "lesson"
)

data class Task(
        val name: String,
        val stepic_id: Int,
        val task_files: Map<String, TaskFile>,
        @field:JsonInclude(JsonInclude.Include.NON_NULL)
        val test_files: Map<String, String>?,
        @field:JsonInclude(JsonInclude.Include.NON_NULL)
        val description_text: String? = null,
        @field:JsonInclude(JsonInclude.Include.NON_NULL)
        val description_format: String? = null,
        @field:JsonInclude(JsonInclude.Include.NON_NULL)
        val additional_files: Map<String, AdditionalFile>,
        val task_type: String = "edu",
        val feedback_link: FeedbackLink
)

data class TaskFile(
        val name: String,
        val text: String,
        val placeholders: List<Placeholder>
)

data class AdditionalFile(
        val text: String,
        @field:JsonProperty(value = "is_visible")
        val visible: Boolean
)

data class Placeholder(
        val offset: Int,
        val length: Int,
        val hints: List<String>,
        val placeholder_text: String
)

data class FeedbackLink(
        val link_type: String,
        @field:JsonInclude(JsonInclude.Include.NON_NULL)
        val link: String? = null)