package `in`.co.abdev.greenguardian.ui.components

import `in`.co.abdev.greenguardian.data.model.Issue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Expected declaration for platform-specific map implementation.
 * Uses Mapbox GL JS via WebView on desktop JVM.
 */
@Composable
expect fun MapView(
    modifier: Modifier = Modifier,
    issues: List<Issue> = emptyList(),
    centerLat: Double = 28.6139,  // Default: New Delhi
    centerLng: Double = 77.2090,
    zoom: Double = 10.0,
    onIssueClick: (String) -> Unit = {}
)
