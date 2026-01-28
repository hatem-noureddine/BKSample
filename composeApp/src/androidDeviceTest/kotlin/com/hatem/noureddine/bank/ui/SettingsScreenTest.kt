package com.hatem.noureddine.bank.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hatem.noureddine.bank.domain.model.AppMode
import com.hatem.noureddine.bank.presentation.viewmodel.settings.SettingsViewModel
import com.hatem.noureddine.bank.ui.screens.SettingsScreen
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun settingsScreen_displaysOptions_andRegistersClick() {
        val state = SettingsViewModel.State(
             currentMode = AppMode.REMOTE
        )

        var intent: SettingsViewModel.Intent? = null

        composeTestRule.setContent {
            SettingsScreen(
                state = state,
                onIntent = { intent = it }
            )
        }

        composeTestRule.onNodeWithText("Settings").assertIsDisplayed()
        composeTestRule.onNodeWithText("Remote (Firebase)").assertIsDisplayed()
        
        // Test interaction
        composeTestRule.onNodeWithText("Mock (Local)").performClick()
        
        // Verify intent was captured (manual assertion since we aren't asserting on a mock object here, 
        // just lambda capture)
        assert(intent is SettingsViewModel.Intent.ChangeMode)
        assert((intent as SettingsViewModel.Intent.ChangeMode).mode == AppMode.MOCK)
    }

    @Test
    fun settingsScreen_displaysMockSelected() {
        val state = SettingsViewModel.State(
            currentMode = AppMode.MOCK
        )

        composeTestRule.setContent {
            SettingsScreen(
                state = state,
                onIntent = {}
            )
        }

        composeTestRule.onNodeWithText("Mock (Local)").assertIsDisplayed()
    }
}
