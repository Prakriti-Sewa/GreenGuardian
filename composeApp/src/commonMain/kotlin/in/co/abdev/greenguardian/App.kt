package `in`.co.abdev.greenguardian

import `in`.co.abdev.greenguardian.ui.screens.HomeScreen
import `in`.co.abdev.greenguardian.ui.theme.GreenGuardianTheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    GreenGuardianTheme {
        HomeScreen(
            onNavigateToReport = {},
            onNavigateToMap = {},
            onNavigateToIssue = {}
        )
    }
}