package `in`.co.abdev.greenguardian.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import `in`.co.abdev.greenguardian.data.model.Issue

/** JS/Web stub implementation - can use Mapbox GL JS directly in browser. */
@Composable
actual fun MapView(
        modifier: Modifier,
        issues: List<Issue>,
        centerLat: Double,
        centerLng: Double,
        zoom: Double,
        onIssueClick: (String) -> Unit
) {
    // For JS target, we could use Mapbox GL JS directly
    // This is a stub - full implementation would create a div and initialize Mapbox
}
