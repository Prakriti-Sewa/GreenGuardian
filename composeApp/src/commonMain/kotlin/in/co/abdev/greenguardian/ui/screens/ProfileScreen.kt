package `in`.co.abdev.greenguardian.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import `in`.co.abdev.greenguardian.ui.viewmodel.AuthUiState
import `in`.co.abdev.greenguardian.ui.viewmodel.ThemeMode
import `in`.co.abdev.greenguardian.ui.viewmodel.ThemeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
        onNavigateBack: () -> Unit,
        themeState: ThemeState,
        onThemeChange: (ThemeMode) -> Unit,
        authState: AuthUiState? = null,
        onLogout: () -> Unit = {},
        onLogin: () -> Unit = {}
) {
        Scaffold(
                topBar = {
                        TopAppBar(
                                title = { Text("Profile") },
                                navigationIcon = {
                                        IconButton(onClick = onNavigateBack) {
                                                Icon(
                                                        Icons.Default.ArrowBack,
                                                        contentDescription = "Back"
                                                )
                                        }
                                },
                                colors =
                                        TopAppBarDefaults.topAppBarColors(
                                                containerColor =
                                                        MaterialTheme.colorScheme.primaryContainer,
                                                titleContentColor =
                                                        MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                        )
                }
        ) { paddingValues ->
                ProfileContent(
                        themeState = themeState,
                        onThemeChange = onThemeChange,
                        authState = authState,
                        onLogout = onLogout,
                        onLogin = onLogin,
                        modifier = Modifier.padding(paddingValues)
                )
        }
}

@Composable
fun ProfileContent(
        themeState: ThemeState,
        onThemeChange: (ThemeMode) -> Unit,
        authState: AuthUiState?,
        onLogout: () -> Unit,
        onLogin: () -> Unit,
        modifier: Modifier = Modifier
) {
        val user = authState?.user
        val isLoggedIn = authState?.isLoggedIn == true

        Column(
                modifier =
                        modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
                // User Info Card
                Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors =
                                CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                )
                ) {
                        Row(
                                modifier = Modifier.fillMaxWidth().padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                                Surface(
                                        modifier = Modifier.size(72.dp),
                                        shape = MaterialTheme.shapes.large,
                                        color = MaterialTheme.colorScheme.primary
                                ) {
                                        Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                        Icons.Default.Person,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(40.dp),
                                                        tint = MaterialTheme.colorScheme.onPrimary
                                                )
                                        }
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                                user?.name ?: "Guest User",
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                                if (isLoggedIn) user?.email ?: ""
                                                else "Login to track your impact",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color =
                                                        MaterialTheme.colorScheme.onPrimaryContainer
                                                                .copy(alpha = 0.7f)
                                        )
                                }
                                if (!isLoggedIn) {
                                        FilledTonalButton(onClick = onLogin) { Text("Login") }
                                }
                        }
                }

                // Stats Card
                Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                                Text(
                                        "Your Impact",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                )
                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                        StatCard(
                                                icon = Icons.Default.Report,
                                                value = "0",
                                                label = "Reported"
                                        )
                                        StatCard(
                                                icon = Icons.Default.CheckCircle,
                                                value = "0",
                                                label = "Resolved"
                                        )
                                        StatCard(
                                                icon = Icons.Default.Star,
                                                value = "0",
                                                label = "Points"
                                        )
                                }
                        }
                }

                // Theme Settings
                Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                                Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                        Icon(
                                                Icons.Default.Palette,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                                "Appearance",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold
                                        )
                                }

                                ThemeOption(
                                        title = "System Default",
                                        subtitle = "Follow system theme",
                                        icon = Icons.Default.SettingsSuggest,
                                        selected = themeState.themeMode == ThemeMode.SYSTEM,
                                        onClick = { onThemeChange(ThemeMode.SYSTEM) }
                                )
                                ThemeOption(
                                        title = "Light Mode",
                                        subtitle = "Always use light theme",
                                        icon = Icons.Default.LightMode,
                                        selected = themeState.themeMode == ThemeMode.LIGHT,
                                        onClick = { onThemeChange(ThemeMode.LIGHT) }
                                )
                                ThemeOption(
                                        title = "Dark Mode",
                                        subtitle = "Always use dark theme",
                                        icon = Icons.Default.DarkMode,
                                        selected = themeState.themeMode == ThemeMode.DARK,
                                        onClick = { onThemeChange(ThemeMode.DARK) }
                                )
                        }
                }

                // App Info
                Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                                Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                        Icon(
                                                Icons.Default.Info,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                                "About",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold
                                        )
                                }
                                Text(
                                        "GreenGuardian v1.0.0",
                                        style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                        "A cross-platform environmental issue reporting app built with Kotlin Multiplatform and Compose.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                        }
                }

                // Logout button
                if (isLoggedIn) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                                onClick = onLogout,
                                colors =
                                        ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.error
                                        ),
                                modifier = Modifier.fillMaxWidth()
                        ) {
                                Icon(Icons.Default.Logout, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Logout")
                        }
                }
        }
}

@Composable
fun StatCard(icon: ImageVector, value: String, label: String) {
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
                Icon(
                        icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                )
                Text(
                        value,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                )
                Text(
                        label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
        }
}

@Composable
fun ThemeOption(
        title: String,
        subtitle: String,
        icon: ImageVector,
        selected: Boolean,
        onClick: () -> Unit
) {
        Card(
                onClick = onClick,
                colors =
                        CardDefaults.cardColors(
                                containerColor =
                                        if (selected) MaterialTheme.colorScheme.primaryContainer
                                        else MaterialTheme.colorScheme.surfaceVariant
                        )
        ) {
                Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                        Icon(
                                icon,
                                contentDescription = null,
                                tint =
                                        if (selected) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Column(modifier = Modifier.weight(1f)) {
                                Text(
                                        title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                )
                                Text(
                                        subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                        }
                        if (selected) {
                                Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                )
                        }
                }
        }
}
