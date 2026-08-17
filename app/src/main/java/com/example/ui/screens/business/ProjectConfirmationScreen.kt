package com.example.ui.screens.business

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.MicroProject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectConfirmationScreen(
    microProject: MicroProject?,
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review Micro-Project") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (microProject == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                Text("Error loading project details.")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                item {
                    Text(microProject.project_title, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Budget", style = MaterialTheme.typography.labelLarge)
                    Text("৳${microProject.budget_min_bdt} - ৳${microProject.budget_max_bdt}")
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text("Duration", style = MaterialTheme.typography.labelLarge)
                    Text("${microProject.duration_days} days")
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Outcome", style = MaterialTheme.typography.titleMedium)
                    Text(microProject.outcome)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                item {
                    Text("Tasks", style = MaterialTheme.typography.titleMedium)
                }
                items(microProject.tasks) { task ->
                    Text("• $task", modifier = Modifier.padding(start = 8.dp, bottom = 4.dp))
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Required Skills", style = MaterialTheme.typography.titleMedium)
                }
                items(microProject.skills) { skill ->
                    AssistChip(
                        onClick = { },
                        label = { Text(skill) },
                        modifier = Modifier.padding(end = 4.dp)
                    )
                }
            }
            
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp)
            ) {
                Text("Confirm & Find Students")
            }
        }
    }
}
