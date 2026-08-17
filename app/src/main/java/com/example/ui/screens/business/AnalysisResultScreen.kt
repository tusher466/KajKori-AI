package com.example.ui.screens.business

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.GeminiService
import com.example.domain.JsonParser
import com.example.domain.MicroProject
import com.example.domain.ProblemAnalysisResult
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisResultScreen(
    problemText: String,
    onBack: () -> Unit,
    onGenerateProject: (MicroProject) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isAnalyzing by remember { mutableStateOf(true) }
    var isGenerating by remember { mutableStateOf(false) }
    var analysisResult by remember { mutableStateOf<ProblemAnalysisResult?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(problemText) {
        val service = GeminiService()
        val json = service.analyzeProblem(problemText)
        val result = JsonParser.parse<ProblemAnalysisResult>(json)
        if (result != null && result.error == null) {
            analysisResult = result
        } else {
            errorMessage = result?.error ?: "Failed to parse AI response"
        }
        isAnalyzing = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Diagnosis") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (isAnalyzing || isGenerating) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(if (isAnalyzing) "Analyzing problem..." else "Generating micro-project...")
                }
            } else if (errorMessage != null) {
                Text(text = "Error: $errorMessage", color = MaterialTheme.colorScheme.error)
            } else {
                analysisResult?.let { result ->
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Triage Result", style = MaterialTheme.typography.labelMedium)
                                Text(result.mode, style = MaterialTheme.typography.headlineSmall)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("Summary", style = MaterialTheme.typography.titleMedium)
                        Text(result.problem_summary)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("Goal", style = MaterialTheme.typography.titleMedium)
                        Text(result.business_goal)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("Reasons", style = MaterialTheme.typography.titleMedium)
                        result.reasons.forEach { reason ->
                            Text("• $reason", modifier = Modifier.padding(start = 8.dp))
                        }
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        if (result.mode == "HUMAN_REQUIRED" || result.mode == "HYBRID") {
                            Button(
                                onClick = {
                                    isGenerating = true
                                    coroutineScope.launch {
                                        val service = GeminiService()
                                        val json = service.generateMicroProject(problemText)
                                        val project = JsonParser.parse<MicroProject>(json)
                                        if (project != null) {
                                            onGenerateProject(project)
                                        } else {
                                            errorMessage = "Failed to generate project"
                                            isGenerating = false
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(56.dp)
                            ) {
                                Text("Create Micro-Project")
                            }
                        } else {
                            Button(
                                onClick = onBack,
                                modifier = Modifier.fillMaxWidth().height(56.dp)
                            ) {
                                Text("Use AI Assistant (Not implemented in MVP)")
                            }
                        }
                    }
                }
            }
        }
    }
}
