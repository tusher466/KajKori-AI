package com.example.ui.screens.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.Project

@Composable
fun StudentDashboardContent(
    onProjectClick: (String) -> Unit
) {
    val sampleProjects = listOf(
        Project(
            id = "1",
            title = "Update Restaurant Menu & Translate to English",
            description = "Need a student to type out our Bengali menu into an Excel sheet and provide an English translation.",
            budget = "৳500 - ৳1000",
            skills = listOf("Data Entry", "Bengali to English Translation"),
            businessName = "Kacchi Bhai (Mirpur)"
        ),
        Project(
            id = "2",
            title = "Create Social Media Posts for New Arrivals",
            description = "Take photos of our new clothing stock and create 5 simple Instagram posts using Canva.",
            budget = "৳800 - ৳1500",
            skills = listOf("Social Media", "Canva", "Basic Photography"),
            businessName = "Deshi Threads"
        )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(sampleProjects) { project ->
            ProjectCard(project = project, onClick = { onProjectClick(project.id) })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProjectCard(project: Project, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(project.title, style = MaterialTheme.typography.titleMedium)
            Text(project.businessName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(project.description, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Budget: ${project.budget}", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                project.skills.take(3).forEach { skill ->
                    AssistChip(
                        onClick = { },
                        label = { Text(skill, style = MaterialTheme.typography.bodySmall) }
                    )
                }
            }
        }
    }
}
