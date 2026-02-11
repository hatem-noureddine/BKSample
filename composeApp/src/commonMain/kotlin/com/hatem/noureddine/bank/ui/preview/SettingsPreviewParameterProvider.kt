package com.hatem.noureddine.bank.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.hatem.noureddine.bank.domain.model.AppMode
import com.hatem.noureddine.bank.presentation.viewmodel.settings.SettingsViewModel

class SettingsPreviewParameterProvider : PreviewParameterProvider<SettingsViewModel.State> {
    override val values: Sequence<SettingsViewModel.State> =
        sequenceOf(
            SettingsViewModel.State(currentMode = AppMode.REMOTE),
            SettingsViewModel.State(currentMode = AppMode.MOCK),
        )
}
