package `in`.co.abdev.greenguardian

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.co.abdev.greenguardian.ui.screens.HomeScreen
import `in`.co.abdev.greenguardian.ui.screens.IssueDetailScreen
import `in`.co.abdev.greenguardian.ui.screens.LoginScreen
import `in`.co.abdev.greenguardian.ui.screens.MapScreen
import `in`.co.abdev.greenguardian.ui.screens.ProfileScreen
import `in`.co.abdev.greenguardian.ui.screens.RegisterScreen
import `in`.co.abdev.greenguardian.ui.screens.ReportIssueScreen
import `in`.co.abdev.greenguardian.ui.theme.GreenGuardianTheme
import `in`.co.abdev.greenguardian.ui.viewmodel.AuthViewModel
import `in`.co.abdev.greenguardian.ui.viewmodel.ThemeMode
import `in`.co.abdev.greenguardian.ui.viewmodel.ThemeViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class Screen {
    Login,
    Register,
    Home,
    ReportIssue,
    Map,
    IssueDetail,
    Profile
}

@Composable
@Preview
fun App(
        themeViewModel: ThemeViewModel = viewModel { ThemeViewModel() },
        authViewModel: AuthViewModel = viewModel { AuthViewModel() }
) {
    var currentScreen by remember { mutableStateOf(Screen.Home) }
    var selectedIssueId by remember { mutableStateOf<String?>(null) }

    val themeState by themeViewModel.themeState.collectAsState()
    val authState by authViewModel.uiState.collectAsState()
    val systemDarkTheme = isSystemInDarkTheme()

    // Navigate to Home when login/register succeeds
    LaunchedEffect(authState.isLoggedIn) {
        if (authState.isLoggedIn &&
                        (currentScreen == Screen.Login || currentScreen == Screen.Register)
        ) {
            currentScreen = Screen.Home
        }
    }

    val isDarkTheme =
            when (themeState.themeMode) {
                ThemeMode.SYSTEM -> systemDarkTheme
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }

    GreenGuardianTheme(darkTheme = isDarkTheme) {
        when (currentScreen) {
            Screen.Login -> {
                LoginScreen(
                        uiState = authState,
                        onLogin = { email, password -> authViewModel.login(email, password) },
                        onNavigateToRegister = { currentScreen = Screen.Register },
                        onClearError = { authViewModel.clearError() }
                )
            }
            Screen.Register -> {
                RegisterScreen(
                        uiState = authState,
                        onRegister = { name, email, password ->
                            authViewModel.register(name, email, password)
                        },
                        onNavigateToLogin = { currentScreen = Screen.Login },
                        onClearError = { authViewModel.clearError() }
                )
            }
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
                        onThemeChange = { themeViewModel.setThemeMode(it) },
                        authState = authState,
                        onLogout = {
                            authViewModel.logout()
                            currentScreen = Screen.Home
                        },
                        onLogin = { currentScreen = Screen.Login }
                )
            }
        }
    }
}
