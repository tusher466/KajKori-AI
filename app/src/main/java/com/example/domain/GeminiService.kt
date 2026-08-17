package com.example.domain

import com.example.BuildConfig
import com.example.data.api.Content
import com.example.data.api.GenerateContentRequest
import com.example.data.api.GenerationConfig
import com.example.data.api.Part
import com.example.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiService {
    private val apiKey = BuildConfig.GEMINI_API_KEY

    suspend fun analyzeProblem(problemDescription: String): String = withContext(Dispatchers.IO) {
        val prompt = """
            You are the KajKori AI Problem Analyzer. 
            Analyze the following business problem and return a JSON object with the following fields:
            - problem_summary: A short summary of the problem.
            - business_goal: The ultimate goal of the business.
            - mode: Either "AI_ONLY", "HUMAN_REQUIRED", or "HYBRID".
            - reasons: An array of strings explaining why you chose this mode.
            
            Business Problem:
            $problemDescription
        """.trimIndent()
        
        callGemini(prompt, isJson = true)
    }

    suspend fun generateMicroProject(problemDescription: String): String = withContext(Dispatchers.IO) {
        val prompt = """
            You are the KajKori AI Micro-Project Generator.
            Generate a micro-project for a student based on this business problem.
            Return a JSON object with the following fields:
            - project_title: A catchy title.
            - problem: The problem statement.
            - outcome: The desired outcome.
            - tasks: An array of strings describing the tasks.
            - skills: An array of strings for required skills.
            - budget_min_bdt: Integer minimum budget.
            - budget_max_bdt: Integer maximum budget.
            - duration_days: Integer expected duration.
            
            Business Problem:
            $problemDescription
        """.trimIndent()
        
        callGemini(prompt, isJson = true)
    }

    private suspend fun callGemini(prompt: String, isJson: Boolean = false): String {
        val request = GenerateContentRequest(
            contents = listOf(
                Content(
                    parts = listOf(Part(text = prompt))
                )
            ),
            generationConfig = if (isJson) GenerationConfig(responseMimeType = "application/json") else null
        )
        
        return try {
            val response = RetrofitClient.service.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            "{ \"error\": \"${e.message}\" }"
        }
    }
}
