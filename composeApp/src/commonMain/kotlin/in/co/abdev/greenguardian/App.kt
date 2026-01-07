package `in`.co.abdev.greenguardian

import `in`.co.abdev.greenguardian.ui.screens.HomeScreen
import `in`.co.abdev.greenguardian.ui.screens.IssueDetailScreen
import `in`.co.abdev.greenguardian.ui.screens.MapScreen
import `in`.co.abdev.greenguardian.ui.screens.ReportIssueScreen
import `in`.co.abdev.greenguardian.ui.theme.GreenGuardianTheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class Screen {
    Home,
    ReportIssue,
    Map,
    IssueDetail
}

@Composable
@Preview
fun App() {
    var currentScreen by remember { mutableStateOf(Screen.Home) }
    var selectedIssueId by remember { mutableStateOf<String?>(null) }
    
    GreenGuardianTheme {
        when (currentScreen) {
            Screen.Home -> {
                HomeScreen(
                    onNavigateToReport = { currentScreen = Screen.ReportIssue },
                    onNavigateToMap = { currentScreen = Screen.Map },
                    onNavigateToIssue = { issueId ->
                        selectedIssueId = issueId
                        currentScreen = Screen.IssueDetail
                    }
                )
            }
            Screen.ReportIssue -> {
                ReportIssueScreen(
                    onNavigateBack = { currentScreen = Screen.Home },
                    onIssueCreated = { _ -> currentScreen = Screen.Home }
                )
            }
            Screen.Map -> {
                MapScreen(
                    onNavigateBack = { currentScreen = Screen.Home },
                    onNavigateToIssue = { issueId ->
                        selectedIssueId = issueId
                        currentScreen = Screen.IssueDetail
                    }
                )
            }
            Screen.IssueDetail -> {
                IssueDetailScreen(
                    issueId = selectedIssueId ?: "",
                    onNavigateBack = { currentScreen = Screen.Home }
                )
            }
        }
    }
}