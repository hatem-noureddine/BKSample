package com.hatem.noureddine.bank.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertDoesNotExist
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.domain.model.CABank
import com.hatem.noureddine.bank.domain.model.BankSections
import com.hatem.noureddine.bank.presentation.viewmodel.accounts.AccountsViewModel
import com.hatem.noureddine.bank.ui.screens.AccountsScreen
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Rule
import org.junit.Test

class AccountsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Test
    fun accountsScreen_displaysHeader_whenLoadingFinished() {
        // Mock state
        val state = AccountsViewModel.State(
            isLoading = false,
            sections = BankSections(emptyList(), emptyList())
        )

        composeTestRule.setContent {
            SharedTransitionLayout {
                AnimatedVisibility(visible = true) {
                    AccountsScreen(
                        state = state,
                        onIntent = {},
                        onAccountClick = {},
                        effectFlow = emptyFlow(),
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this
                    )
                }
            }
        }

        // Verify UI
        composeTestRule.onNodeWithText("MES COMPTES").assertIsDisplayed()
        composeTestRule.onNodeWithText("Bonjour,").assertIsDisplayed()
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Test
    fun accountsScreen_showsSnackbar_onErrorEffect() {
        val state = AccountsViewModel.State(isLoading = false)

        composeTestRule.setContent {
            SharedTransitionLayout {
                AnimatedVisibility(visible = true) {
                    AccountsScreen(
                        state = state,
                        onIntent = {},
                        onAccountClick = {},
                        effectFlow = flowOf(AccountsViewModel.Effect.ShowError("Network error")),
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this,
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("Network error").assertIsDisplayed()
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Test
    fun accountsScreen_expandedBank_showsAccounts() {
        val account =
            Account(
                id = "acc1",
                order = 1,
                holder = "Holder",
                role = 1,
                contractNumber = "123",
                label = "Compte Courant",
                productCode = "CODE",
                balance = 10.0,
                operations = emptyList(),
            )
        val bank = CABank(name = "CA", accounts = listOf(account))
        val state =
            AccountsViewModel.State(
                isLoading = false,
                sections = BankSections(listOf(bank), emptyList()),
                expandedBankNames = setOf("CA"),
            )

        composeTestRule.setContent {
            SharedTransitionLayout {
                AnimatedVisibility(visible = true) {
                    AccountsScreen(
                        state = state,
                        onIntent = {},
                        onAccountClick = {},
                        effectFlow = emptyFlow(),
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this,
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("Compte Courant").assertIsDisplayed()
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Test
    fun accountsScreen_clickOnSectionHeader_triggersToggleIntent() {
        val bank = CABank(name = "CA", accounts = emptyList())
        val state =
            AccountsViewModel.State(
                isLoading = false,
                sections = BankSections(listOf(bank), emptyList()),
                expandedBankNames = emptySet(),
            )
        var intent: AccountsViewModel.Intent? = null

        composeTestRule.setContent {
            SharedTransitionLayout {
                AnimatedVisibility(visible = true) {
                    AccountsScreen(
                        state = state,
                        onIntent = { intent = it },
                        onAccountClick = {},
                        effectFlow = emptyFlow(),
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this,
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("CA").performClick()

        assert(intent is AccountsViewModel.Intent.ToggleBankSection)
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Test
    fun accountsScreen_collapsedBank_hidesAccounts() {
        val account =
            Account(
                id = "acc1",
                order = 1,
                holder = "Holder",
                role = 1,
                contractNumber = "123",
                label = "Compte Courant",
                productCode = "CODE",
                balance = 10.0,
                operations = emptyList(),
            )
        val bank = CABank(name = "CA", accounts = listOf(account))
        val state =
            AccountsViewModel.State(
                isLoading = false,
                sections = BankSections(listOf(bank), emptyList()),
                expandedBankNames = emptySet(),
            )

        composeTestRule.setContent {
            SharedTransitionLayout {
                AnimatedVisibility(visible = true) {
                    AccountsScreen(
                        state = state,
                        onIntent = {},
                        onAccountClick = {},
                        effectFlow = emptyFlow(),
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this,
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("Compte Courant").assertDoesNotExist()
    }
}
