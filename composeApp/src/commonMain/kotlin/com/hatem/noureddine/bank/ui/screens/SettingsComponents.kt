package com.hatem.noureddine.bank.ui.screens

import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Cloud
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.hatem.noureddine.bank.domain.model.AppMode
import com.hatem.noureddine.bank.presentation.viewmodel.settings.SettingsViewModel

/**
 * Section for selecting the Data Source (Remote vs Mock).
 *
 * @param currentMode The currently selected [AppMode].
 * @param onIntent Callback to send intents to the ViewModel.
 */
@Composable
fun DataSourceSection(
    currentMode: AppMode,
    onIntent: (SettingsViewModel.Intent) -> Unit,
) {
    Text(
        text = "DATA SOURCE",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
    )

    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            SettingsItem(
                title = "Remote (Firebase)",
                subtitle = "Fetch data from live network",
                icon = Icons.Rounded.Cloud,
                selected = currentMode == AppMode.REMOTE,
                onClick = { onIntent(SettingsViewModel.Intent.ChangeMode(AppMode.REMOTE)) },
            )

            // Divider or Spacer?

            SettingsItem(
                title = "Mock (Local)",
                subtitle = "Use embedded JSON data (Offline)",
                icon = Icons.Rounded.Storage,
                selected = currentMode == AppMode.MOCK,
                onClick = { onIntent(SettingsViewModel.Intent.ChangeMode(AppMode.MOCK)) },
            )
        }
    }
}

/**
 * Displays informational text about the settings.
 */
@Composable
fun InfoSection() {
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Changing the data source will reload the application state.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SettingsItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        RadioButton(
            selected = selected,
            onClick = onClick,
        )
    }
}

@Preview
@Composable
fun DataSourceSectionPreview() {
    DataSourceSection(
        currentMode = AppMode.MOCK,
        onIntent = {}
    )
}
