package `in`.co.abdev.greenguardian.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import `in`.co.abdev.greenguardian.data.model.Issue

/** WasmJS stub implementation. */
@Composable
actual fun MapView(
        modifier: Modifier,
        issues: List<Issue>,
        centerLat: Double,
        centerLng: Double,
        zoom: Double,
        onIssueClick: (String) -> Unit
) {
    // WasmJS stub - would need WebView or canvas-based rendering
}
