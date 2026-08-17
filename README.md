# KajKori AI

Bangladesh-focused AI-powered human-execution marketplace that connects students with small/local businesses.

## Overview
KajKori AI is an MVP platform designed to turn business problems into actionable micro-projects for students when human work adds value. It utilizes the Gemini API to diagnose problems, structure projects, and assist students, while providing local context and human execution.

## Features
- **Role-based Access:** Student and Business personas.
- **AI Problem Analyzer:** Business users describe a problem, and the Gemini API analyzes it to determine if it requires AI-only, Human, or Hybrid execution.
- **AI Micro-Project Generator:** For tasks requiring humans, Gemini automatically generates a structured micro-project with titles, required skills, and estimated budget.
- **Student Dashboard:** Students can browse available projects based on required skills and apply.

## Setup Instructions

### 1. Prerequisites
- Android Studio or AI Studio environment.
- Gemini API Key.

### 2. Gemini API Configuration
1. Open the **Secrets panel in AI Studio** and enter your Gemini API key under `GEMINI_API_KEY`.
2. In the codebase, the `.env.example` file declares `GEMINI_API_KEY`. It is injected into `BuildConfig.GEMINI_API_KEY` at runtime via the Secrets Gradle Plugin.
3. Keep the key out of public repositories; do not modify `local.properties` manually in AI Studio.

### 3. Firebase Setup
For full production deployment (not fully implemented in this MVP but structured for it):
1. Navigate to the Firebase Console and create a new project.
2. Enable **Firestore**, **Authentication** (Google Sign-In), and **App Check**.
3. Integrate the required Firebase libraries found in `app/build.gradle.kts`. Uncomment the Firebase dependencies when ready.

### 4. Running the MVP
1. Run the `compile_applet` command or click the **Run** button in the AI Studio platform.
2. The application will start on the **Login Screen**.
3. **Demo Flow for Business:**
   - Click "I am a Business" -> Arrive at Business Dashboard.
   - Click "+" to describe a problem (e.g. "I need my restaurant menu typed in English").
   - Click "Analyze Problem" -> Gemini parses the problem.
   - Click "Create Micro-Project" -> Gemini generates project parameters.
   - Confirm and list the project.
4. **Demo Flow for Student:**
   - Click "I am a Student" -> Arrive at Student Dashboard.
   - View seeded projects.
   - Tap a project to view details and apply.

## Architecture
- **UI:** Jetpack Compose (Material 3).
- **Navigation:** Jetpack Navigation Compose.
- **Network/API:** Retrofit + Moshi for calling Gemini REST API.
- **Concurrency:** Kotlin Coroutines.
