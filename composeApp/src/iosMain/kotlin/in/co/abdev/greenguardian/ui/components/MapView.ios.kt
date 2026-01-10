package `in`.co.abdev.greenguardian.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.co.abdev.greenguardian.data.model.Issue

/** iOS stub implementation - Mapbox iOS SDK should be integrated separately. */
@Composable
actual fun MapView(
        modifier: Modifier,
        issues: List<Issue>,
        centerLat: Double,
        centerLng: Double,
        zoom: Double,
        onIssueClick: (String) -> Unit
) {
    Box(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
    ) {
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                    Icons.Default.Map,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
            )
            Text("Mapbox integration for iOS")
            Text(
                    "${issues.size} issues to display",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
