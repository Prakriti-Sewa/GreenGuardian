package `in`.co.abdev.greenguardian

import `in`.co.abdev.greenguardian.ui.screens.HomeScreen
import `in`.co.abdev.greenguardian.ui.screens.IssueDetailScreen
import `in`.co.abdev.greenguardian.ui.screens.MapScreen
import `in`.co.abdev.greenguardian.ui.screens.ProfileScreen
import `in`.co.abdev.greenguardian.ui.screens.ReportIssueScreen
import `in`.co.abdev.greenguardian.ui.theme.GreenGuardianTheme
import `in`.co.abdev.greenguardian.ui.viewmodel.ThemeMode
import `in`.co.abdev.greenguardian.ui.viewmodel.ThemeViewModel
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class Screen {
    Home,
    ReportIssue,
    Map,
    IssueDetail,
    Profile
}

@Composable
@Preview
fun App(themeViewModel: ThemeViewModel = viewModel { ThemeViewModel() }) {
    var currentScreen by remember { mutableStateOf(Screen.Home) }
    var selectedIssueId by remember { mutableStateOf<String?>(null) }
    
    val themeState by themeViewModel.themeState.collectAsState()
    val systemDarkTheme = isSystemInDarkTheme()
    
    val isDarkTheme = when (themeState.themeMode) {
        ThemeMode.SYSTEM -> systemDarkTheme
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    
    GreenGuardianTheme(darkTheme = isDarkTheme) {
        when (currentScreen) {
            Screen.Home -> {
                HomeScreen(
                    onNavigateToReport = { currentScreen = Screen.ReportIssue },
                    onNavigateToMap = { currentScreen = Screen.Map },
                    onNavigateToIssue = { issueId ->
                        selectedIssueId = issueId
                        currentScreen = Screen.IssueDetail
                    },
                    onNavigateToProfile = { currentScreen = Screen.Profile }
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
            Screen.Profile -> {
                ProfileScreen(
                    onNavigateBack = { currentScreen = Screen.Home },
                    themeState = themeState,
                    onThemeChange = { themeViewModel.setThemeMode(it) }
                )
            }
        }
    }
}