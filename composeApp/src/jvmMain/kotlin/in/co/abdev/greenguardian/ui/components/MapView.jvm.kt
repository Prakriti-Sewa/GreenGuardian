package `in`.co.abdev.greenguardian.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import `in`.co.abdev.greenguardian.data.model.Issue
import javax.imageio.ImageIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
actual fun MapView(
        modifier: Modifier,
        issues: List<Issue>,
        centerLat: Double,
        centerLng: Double,
        zoom: Double,
        onIssueClick: (String) -> Unit
) {
    var mapImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var retryKey by remember { mutableStateOf(0) }

    val mapboxToken = System.getenv("MAPBOX_TOKEN")

    // Show error if token not configured
    if (mapboxToken.isNullOrBlank()) {
        Box(
                modifier =
                        modifier.fillMaxSize().background(MaterialTheme.colorScheme.errorContainer),
                contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                        Icons.Default.Map,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                        "MAPBOX_TOKEN not configured",
                        color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                        "Set MAPBOX_TOKEN environment variable",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                )
            }
        }
        return
    }

    val staticMapUrl =
            remember(issues, centerLat, centerLng, zoom) {
                buildStaticMapUrl(
                        token = mapboxToken,
                        centerLat = centerLat,
                        centerLng = centerLng,
                        zoom = zoom.toInt(),
                        width = 800,
                        height = 600,
                        issues = issues
                )
            }

    LaunchedEffect(staticMapUrl, retryKey) {
        isLoading = true
        error = null
        println("Loading map from: $staticMapUrl")
        try {
            mapImage =
                    withContext(Dispatchers.IO) {
                        val url = java.net.URI(staticMapUrl).toURL()
                        val connection = url.openConnection() as java.net.HttpURLConnection
                        connection.connectTimeout = 10000
                        connection.readTimeout = 10000
                        connection.setRequestProperty("User-Agent", "GreenGuardian/1.0")
                        val inputStream = connection.inputStream
                        val bufferedImage = ImageIO.read(inputStream)
                        inputStream.close()
                        println("Map loaded successfully")
                        bufferedImage?.toComposeImageBitmap()
                    }
            if (mapImage == null) {
                error = "Failed to decode image"
            }
            isLoading = false
        } catch (e: Exception) {
            println("Map loading error: ${e.message}")
            error = e.message ?: "Unknown error"
            isLoading = false
        }
    }

    Box(modifier = modifier) {
        when {
            isLoading -> {
                Box(
                        modifier =
                                Modifier.fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Loading map...")
                    }
                }
            }
            error != null -> {
                Box(
                        modifier =
                                Modifier.fillMaxSize()
                                        .background(MaterialTheme.colorScheme.errorContainer),
                        contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                                Icons.Default.Map,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                                "Could not load map",
                                color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                                error ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color =
                                        MaterialTheme.colorScheme.onErrorContainer.copy(
                                                alpha = 0.7f
                                        )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        FilledTonalButton(onClick = { retryKey++ }) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Retry")
                        }
                    }
                }
            }
            mapImage != null -> {
                Image(
                        bitmap = mapImage!!,
                        contentDescription = "Map showing ${issues.size} issues",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                )

                Surface(
                        modifier = Modifier.align(Alignment.TopStart).padding(8.dp),
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
                ) {
                    Text(
                            "${issues.size} issues",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

private fun buildStaticMapUrl(
        token: String,
        centerLat: Double,
        centerLng: Double,
        zoom: Int,
        width: Int,
        height: Int,
        issues: List<Issue>
): String {
    val markers =
            issues.take(50).joinToString(",") { issue ->
                "pin-s+22c55e(${issue.longitude},${issue.latitude})"
            }

    val markerPath = if (markers.isNotEmpty()) "$markers/" else ""

    return "https://api.mapbox.com/styles/v1/mapbox/streets-v12/static/$markerPath$centerLng,$centerLat,$zoom/${width}x${height}@2x?access_token=$token"
}
