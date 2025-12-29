package `in`.co.abdev.greenguardian.ui.screens

import `in`.co.abdev.greenguardian.data.model.IssueCategory
import `in`.co.abdev.greenguardian.data.model.IssueSeverity
import `in`.co.abdev.greenguardian.ui.viewmodel.ReportIssueViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun ReportIssueScreen(
    onNavigateBack: () -> Unit,
    onIssueCreated: (String) -> Unit,
    viewModel: ReportIssueViewModel = viewModel { ReportIssueViewModel() }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            uiState.createdIssue?.let { issue ->
                onIssueCreated(issue.id)
                viewModel.resetForm()
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report Environmental Issue") },
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
        ReportIssueContent(
            uiState = uiState,
            onTitleChange = viewModel::updateTitle,
            onDescriptionChange = viewModel::updateDescription,
            onCategoryChange = viewModel::updateCategory,
            onSeverityChange = viewModel::updateSeverity,
            onLocationSelected = { lat, lng -> viewModel.updateLocation(lat, lng) },
            onImageSelected = viewModel::updateImage,
            onSubmit = viewModel::submitIssue,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportIssueContent(
    uiState: `in`.co.abdev.greenguardian.ui.viewmodel.ReportIssueUiState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategoryChange: (IssueCategory) -> Unit,
    onSeverityChange: (IssueSeverity) -> Unit,
    onLocationSelected: (Double, Double) -> Unit,
    onImageSelected: (String?) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title Input
        OutlinedTextField(
            value = uiState.title,
            onValueChange = onTitleChange,
            label = { Text("Title") },
            placeholder = { Text("What's the issue?") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Default.Title, contentDescription = null)
            }
        )
        
        // Description Input
        OutlinedTextField(
            value = uiState.description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            placeholder = { Text("Describe the environmental issue in detail") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            maxLines = 6,
            leadingIcon = {
                Icon(Icons.Default.Description, contentDescription = null)
            }
        )
        
        // Category Selection
        var categoryExpanded by remember { mutableStateOf(false) }
        
        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = !categoryExpanded }
        ) {
            OutlinedTextField(
                value = uiState.category.name.replace("_", " "),
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                leadingIcon = {
                    Icon(Icons.Default.Category, contentDescription = null)
                }
            )
            
            ExposedDropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                IssueCategory.entries.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name.replace("_", " ")) },
                        onClick = {
                            onCategoryChange(category)
                            categoryExpanded = false
                        }
                    )
                }
            }
        }
        
        // Severity Selection
        Text(
            text = "Severity",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IssueSeverity.entries.forEach { severity ->
                FilterChip(
                    selected = uiState.severity == severity,
                    onClick = { onSeverityChange(severity) },
                    label = { Text(severity.name) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // Location Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Location",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                if (uiState.latitude != null && uiState.longitude != null) {
                    Text(
                        "Lat: ${uiState.latitude}, Lng: ${uiState.longitude}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    Text(
                        "No location selected",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Button(
                    onClick = {
                        // TODO: Implement location picker
                        // For now, use a mock location
                        onLocationSelected(28.6139, 77.2090) // Delhi coordinates
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Get Current Location")
                }
            }
        }
        
        // Image Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Photo,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Photo (Optional)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                if (uiState.imageBase64 != null) {
                    Text(
                        "Photo attached",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedButton(
                        onClick = { onImageSelected(null) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Remove Photo")
                    }
                } else {
                    OutlinedButton(
                        onClick = {
                            // TODO: Implement image picker
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Photo")
                    }
                }
            }
        }
        
        // Error Message
        if (uiState.error != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = uiState.error,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        
        // Submit Button
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !uiState.isSubmitting
        ) {
            if (uiState.isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Icon(Icons.Default.Send, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Submit Report")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
