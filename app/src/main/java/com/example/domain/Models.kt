package com.example.domain

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProblemAnalysisResult(
    val problem_summary: String = "",
    val business_goal: String = "",
    val mode: String = "",
    val reasons: List<String> = emptyList(),
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class MicroProject(
    val project_title: String = "",
    val problem: String = "",
    val outcome: String = "",
    val tasks: List<String> = emptyList(),
    val skills: List<String> = emptyList(),
    val budget_min_bdt: Int = 0,
    val budget_max_bdt: Int = 0,
    val duration_days: Int = 0,
    val error: String? = null
)

// Main Domain Models
data class Project(
    val id: String,
    val title: String,
    val description: String,
    val budget: String,
    val skills: List<String>,
    val businessName: String,
    val status: ProjectStatus = ProjectStatus.OPEN
)

enum class ProjectStatus {
    OPEN, IN_PROGRESS, COMPLETED
}

data class UserSession(
    val role: UserRole
)

enum class UserRole {
    STUDENT, BUSINESS
}
