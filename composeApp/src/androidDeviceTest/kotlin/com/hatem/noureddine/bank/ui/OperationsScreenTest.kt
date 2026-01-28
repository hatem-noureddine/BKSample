package com.hatem.noureddine.bank.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.domain.model.Operation
import com.hatem.noureddine.bank.presentation.viewmodel.operations.OperationsViewModel
import com.hatem.noureddine.bank.ui.screens.OperationsContent
import org.junit.Rule
import org.junit.Test

class OperationsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Test
    fun operationsScreen_displaysBalance_whenLoaded() {
        val account = Account(
            id = "1",
            order = 1,
            holder = "Holder",
            role = 1,
            contractNumber = "123",
            label = "Test Account",
            productCode = "Code",
            balance = 2500.0,
            operations = emptyList()
        )
        
        val state = OperationsViewModel.State(
            isLoading = false,
            account = account,
            groupedOperations = emptyMap()
        )

        composeTestRule.setContent {
            SharedTransitionLayout {
                AnimatedVisibility(visible = true) {
                    OperationsContent(
                        state = state,
                        onBackClick = {},
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("TEST ACCOUNT").assertIsDisplayed()
        composeTestRule.onNodeWithText("2500.0 €").assertIsDisplayed()
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Test
    fun operationsScreen_displaysErrorState() {
        val state =
            OperationsViewModel.State(
                isLoading = false,
                error = "Something went wrong",
            )

        composeTestRule.setContent {
            SharedTransitionLayout {
                AnimatedVisibility(visible = true) {
                    OperationsContent(
                        state = state,
                        onBackClick = {},
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this,
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Test
    fun operationsScreen_displaysGroupedHeaders() {
        val ops =
            listOf(
                Operation("op1", "Op1", 10.0, "cat", 1672531200L),
                Operation("op2", "Op2", 20.0, "cat", 1672617600L),
            )
        val state =
            OperationsViewModel.State(
                isLoading = false,
                operations = ops,
                groupedOperations =
                    mapOf(
                        "01 Jan 2023" to listOf(ops[0]),
                        "02 Jan 2023" to listOf(ops[1]),
                    ),
            )

        composeTestRule.setContent {
            SharedTransitionLayout {
                AnimatedVisibility(visible = true) {
                    OperationsContent(
                        state = state,
                        onBackClick = {},
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this,
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("01 JAN 2023").assertIsDisplayed()
        composeTestRule.onNodeWithText("02 JAN 2023").assertIsDisplayed()
    }
}
