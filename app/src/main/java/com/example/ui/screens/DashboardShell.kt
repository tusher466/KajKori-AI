package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.UserRole
import com.example.ui.screens.business.BusinessDashboardContent
import com.example.ui.screens.student.StudentDashboardContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardShell(
    initialRole: UserRole = UserRole.STUDENT,
    onNavigateToProblemIntake: () -> Unit,
    onNavigateToProjectDetails: (String) -> Unit
) {
    var currentRole by remember { mutableStateOf(initialRole) }
    var currentTab by remember { mutableStateOf("Home") }
    var showRoleMenu by remember { mutableStateOf(false) }

    BoxWithConstraints {
        val isWideScreen = maxWidth > 600.dp

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Text("KajKori AI - ${if (currentRole == UserRole.BUSINESS) "Business" else "Student"}") 
                    },
                    actions = {
                        Box {
                            IconButton(onClick = { showRoleMenu = true }) {
                                Icon(Icons.Default.AccountCircle, contentDescription = "Switch Role")
                            }
                            DropdownMenu(
                                expanded = showRoleMenu,
                                onDismissRequest = { showRoleMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("View as Student") },
                                    onClick = { 
                                        currentRole = UserRole.STUDENT
                                        showRoleMenu = false 
                                    },
                                    leadingIcon = { Icon(Icons.Default.School, contentDescription = null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("View as Business") },
                                    onClick = { 
                                        currentRole = UserRole.BUSINESS
                                        showRoleMenu = false 
                                    },
                                    leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) }
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            },
            bottomBar = {
                if (!isWideScreen) {
                    NavigationBar {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home") },
                            selected = currentTab == "Home",
                            onClick = { currentTab = "Home" }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.List, contentDescription = "Projects") },
                            label = { Text("Projects") },
                            selected = currentTab == "Projects",
                            onClick = { currentTab = "Projects" }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                            label = { Text("Profile") },
                            selected = currentTab == "Profile",
                            onClick = { currentTab = "Profile" }
                        )
                    }
                }
            },
            floatingActionButton = {
                if (currentRole == UserRole.BUSINESS && currentTab == "Home") {
                    FloatingActionButton(onClick = onNavigateToProblemIntake) {
                        Icon(Icons.Default.Add, contentDescription = "Add Problem")
                    }
                }
            }
        ) { padding ->
            Row(modifier = Modifier.fillMaxSize().padding(padding)) {
                if (isWideScreen) {
                    NavigationRail(
                        modifier = Modifier.width(80.dp),
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        NavigationRailItem(
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text("Home") },
                            selected = currentTab == "Home",
                            onClick = { currentTab = "Home" }
                        )
                        NavigationRailItem(
                            icon = { Icon(Icons.Default.List, contentDescription = "Projects") },
                            label = { Text("Projects") },
                            selected = currentTab == "Projects",
                            onClick = { currentTab = "Projects" }
                        )
                        NavigationRailItem(
                            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                            label = { Text("Profile") },
                            selected = currentTab == "Profile",
                            onClick = { currentTab = "Profile" }
                        )
                    }
                }
                
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    when (currentTab) {
                        "Home" -> {
                            if (currentRole == UserRole.BUSINESS) {
                                BusinessDashboardContent()
                            } else {
                                StudentDashboardContent(onProjectClick = onNavigateToProjectDetails)
                            }
                        }
                        "Projects" -> {
                            CenterText("My Projects (Coming Soon)")
                        }
                        "Profile" -> {
                            CenterText("User Profile & Wallet (Coming Soon)")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CenterText(text: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
