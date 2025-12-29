package `in`.co.abdev.greenguardian.ui.screens

import `in`.co.abdev.greenguardian.data.model.Issue
import `in`.co.abdev.greenguardian.data.repository.IssueRepository
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssueDetailScreen(
    issueId: String,
    onNavigateBack: () -> Unit
) {
    var issue by remember { mutableStateOf<Issue?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    
    val repository = remember { IssueRepository() }
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(issueId) {
        scope.launch {
            isLoading = true
            repository.getIssueById(issueId)
                .onSuccess {
                    issue = it
                    isLoading = false
                }
                .onFailure {
                    error = it.message
                    isLoading = false
                }
        }
    }
    
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
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
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
                            text = error ?: "Unknown error",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            issue != null -> {
                IssueDetailContent(
                    issue = issue!!,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
fun IssueDetailContent(
    issue: Issue,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Header with status
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = issue.title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IssueStatusBadge(status = issue.status)
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IssueCategoryChip(category = issue.category)
                    SeverityChip(severity = issue.severity)
                }
            }
        }
        
        // Details Section
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Description
            DetailSection(
                icon = Icons.Default.Description,
                title = "Description"
            ) {
                Text(
                    text = issue.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            
            // Location
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
                        Text(
                            text = "Latitude: ${issue.latitude}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Longitude: ${issue.longitude}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    OutlinedButton(onClick = { /* TODO: Open in maps */ }) {
                        Icon(Icons.Default.Map, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("View")
                    }
                }
            }
            
            // Timeline
            DetailSection(
                icon = Icons.Default.Timeline,
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
            
            // Image placeholder
            if (issue.imageUrl != null) {
                DetailSection(
                    icon = Icons.Default.Photo,
                    title = "Photo"
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                MaterialTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Image,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "Image loading...",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailSection(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
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
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
fun SeverityChip(severity: `in`.co.abdev.greenguardian.data.model.IssueSeverity) {
    val color = when (severity) {
        `in`.co.abdev.greenguardian.data.model.IssueSeverity.LOW -> MaterialTheme.colorScheme.tertiary
        `in`.co.abdev.greenguardian.data.model.IssueSeverity.MEDIUM -> MaterialTheme.colorScheme.primary
        `in`.co.abdev.greenguardian.data.model.IssueSeverity.HIGH -> MaterialTheme.colorScheme.secondary
        `in`.co.abdev.greenguardian.data.model.IssueSeverity.CRITICAL -> MaterialTheme.colorScheme.error
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
    val date = Date(timestamp)
    val format = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
    return format.format(date)
}
