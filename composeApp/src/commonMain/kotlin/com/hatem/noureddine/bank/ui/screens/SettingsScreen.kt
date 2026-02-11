package com.hatem.noureddine.bank.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.hatem.noureddine.bank.domain.model.AppMode
import com.hatem.noureddine.bank.presentation.viewmodel.settings.SettingsViewModel
import com.hatem.noureddine.bank.ui.preview.SettingsPreviewParameterProvider

/**
 * Settings screen for configuring application behavior.
 * Allows switching between Remote (API) and Mock (Local JSON) data sources.
 *
 * @param state The current UI state from the ViewModel.
 * @param onIntent Callback to send intents to the ViewModel.
 */
@Composable
fun SettingsScreen(
    state: SettingsViewModel.State,
    onIntent: (SettingsViewModel.Intent) -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxWidth(),
        ) {
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Spacer(modifier = Modifier.height(24.dp))

            DataSourceSection(
                currentMode = state.currentMode ?: AppMode.MOCK,
                onIntent = onIntent,
            )

            Spacer(modifier = Modifier.height(24.dp))

            InfoSection()
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview(
    @PreviewParameter(SettingsPreviewParameterProvider::class) state: SettingsViewModel.State,
) {
    SettingsScreen(
        state = state,
        onIntent = {},
    )
}
