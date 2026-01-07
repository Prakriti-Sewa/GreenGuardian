package `in`.co.abdev.greenguardian.ui.screens

import `in`.co.abdev.greenguardian.data.model.Issue
import `in`.co.abdev.greenguardian.data.model.IssueCategory
import `in`.co.abdev.greenguardian.data.model.IssueSeverity
import `in`.co.abdev.greenguardian.data.model.IssueStatus
import `in`.co.abdev.greenguardian.ui.viewmodel.IssueDetailUiState
import `in`.co.abdev.greenguardian.ui.viewmodel.IssueDetailViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueDetailScreen(
    issueId: String,
    onNavigateBack: () -> Unit,
    viewModel: IssueDetailViewModel = viewModel { IssueDetailViewModel(issueId) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Issue Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        IssueDetailContent(
            uiState = uiState,
            onRetry = { viewModel.loadIssue() },
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun IssueDetailContent(
    uiState: IssueDetailUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        uiState.error != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRetry) {
                        Text("Retry")
                    }
                }
            }
        }
        uiState.issue != null -> {
            IssueDetails(
                issue = uiState.issue,
                modifier = modifier
            )
        }
    }
}

@Composable
fun IssueDetails(
    issue: Issue,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = MaterialTheme.shapes.large.copy(
                topStart = androidx.compose.foundation.shape.CornerSize(0.dp),
                topEnd = androidx.compose.foundation.shape.CornerSize(0.dp)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = issue.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IssueStatusBadge(status = issue.status)
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IssueCategoryChip(category = issue.category)
                    SeverityChip(severity = issue.severity)
                }
            }
        }
        
        // Details
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DetailSection(
                icon = Icons.Default.Description,
                title = "Description"
            ) {
                Text(
                    text = issue.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            DetailSection(
                icon = Icons.Default.LocationOn,
                title = "Location"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Latitude: ${issue.latitude}", style = MaterialTheme.typography.bodyMedium)
                        Text("Longitude: ${issue.longitude}", style = MaterialTheme.typography.bodyMedium)
                    }
                    OutlinedButton(onClick = { /* TODO: Open in maps */ }) {
                        Icon(Icons.Default.Map, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("View")
                    }
                }
            }
            
            DetailSection(
                icon = Icons.Default.Schedule,
                title = "Timeline"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    TimelineItem(
                        icon = Icons.Default.Report,
                        label = "Reported",
                        timestamp = issue.reportedAt,
                        user = issue.reportedBy
                    )
                    
                    if (issue.verifiedAt != null) {
                        TimelineItem(
                            icon = Icons.Default.Verified,
                            label = "Verified",
                            timestamp = issue.verifiedAt
                        )
                    }
                    
                    if (issue.resolvedAt != null) {
                        TimelineItem(
                            icon = Icons.Default.CheckCircle,
                            label = "Resolved",
                            timestamp = issue.resolvedAt
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DetailSection(
    icon: ImageVector,
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            content()
        }
    }
}

@Composable
fun TimelineItem(
    icon: ImageVector,
    label: String,
    timestamp: Long,
    user: String? = null
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formatTimestamp(timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            user?.let {
                Text(
                    text = "by $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun SeverityChip(severity: IssueSeverity) {
    val color = when (severity) {
        IssueSeverity.LOW -> MaterialTheme.colorScheme.tertiary
        IssueSeverity.MEDIUM -> MaterialTheme.colorScheme.primary
        IssueSeverity.HIGH -> MaterialTheme.colorScheme.secondary
        IssueSeverity.CRITICAL -> MaterialTheme.colorScheme.error
    }
    
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = color
            )
            Text(
                text = severity.name,
                style = MaterialTheme.typography.labelSmall,
                color = color
            )
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    // Simple formatting - in production, use kotlinx-datetime
    val seconds = timestamp / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24
    
    return when {
        days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
        hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
        minutes > 0 -> "$minutes minute${if (minutes > 1) "s" else ""} ago"
        else -> "Just now"
    }
}
