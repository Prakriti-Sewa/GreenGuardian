package `in`.co.abdev.greenguardian.ui.screens

import `in`.co.abdev.greenguardian.data.model.Issue
import `in`.co.abdev.greenguardian.data.model.IssueStatus
import `in`.co.abdev.greenguardian.ui.viewmodel.IssuesUiState
import `in`.co.abdev.greenguardian.ui.viewmodel.IssuesViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToReport: () -> Unit,
    onNavigateToMap: () -> Unit,
    onNavigateToIssue: (String) -> Unit,
    viewModel: IssuesViewModel = viewModel { IssuesViewModel() }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("GreenGuardian")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToReport,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Report Issue")
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Home") },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Map, contentDescription = null) },
                    label = { Text("Map") },
                    selected = false,
                    onClick = onNavigateToMap
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = { }
                )
            }
        }
    ) { paddingValues ->
        HomeContent(
            uiState = uiState,
            onRefresh = { viewModel.loadIssues() },
            onIssueClick = onNavigateToIssue,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun HomeContent(
    uiState: IssuesUiState,
    onRefresh: () -> Unit,
    onIssueClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Stats Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.Report,
                    label = "Total Issues",
                    value = uiState.issues.size.toString()
                )
                StatItem(
                    icon = Icons.Default.Pending,
                    label = "Pending",
                    value = uiState.issues.count { it.status == IssueStatus.SUBMITTED }.toString()
                )
                StatItem(
                    icon = Icons.Default.CheckCircle,
                    label = "Resolved",
                    value = uiState.issues.count { it.status == IssueStatus.RESOLVED }.toString()
                )
            }
        }
        
        // Issues List
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.error,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onRefresh) {
                            Text("Retry")
                        }
                    }
                }
            }
            uiState.issues.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No issues reported yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Be the first to protect the environment!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.issues) { issue ->
                        IssueCard(
                            issue = issue,
                            onClick = { onIssueClick(issue.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun IssueCard(
    issue: Issue,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = issue.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = issue.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2
                    )
                }
                IssueStatusBadge(status = issue.status)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${issue.latitude.format(2)}, ${issue.longitude.format(2)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                IssueCategoryChip(category = issue.category)
            }
        }
    }
}

@Composable
fun IssueStatusBadge(status: IssueStatus) {
    val (color, text) = when (status) {
        IssueStatus.SUBMITTED -> MaterialTheme.colorScheme.secondary to "Submitted"
        IssueStatus.VERIFIED -> MaterialTheme.colorScheme.primary to "Verified"
        IssueStatus.IN_PROGRESS -> MaterialTheme.colorScheme.tertiary to "In Progress"
        IssueStatus.RESOLVED -> MaterialTheme.colorScheme.primary to "Resolved"
        IssueStatus.REJECTED -> MaterialTheme.colorScheme.error to "Rejected"
    }
    
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
fun IssueCategoryChip(category: `in`.co.abdev.greenguardian.data.model.IssueCategory) {
    val text = category.name.replace("_", " ").lowercase().capitalize()
    
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}

fun Double.format(decimals: Int): String {
    return "%.${decimals}f".format(this)
}

fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
