package com.hatem.noureddine.bank.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.domain.model.BankSections
import com.hatem.noureddine.bank.domain.model.CABank
import com.hatem.noureddine.bank.presentation.viewmodel.accounts.AccountsViewModel
import com.hatem.noureddine.bank.ui.screens.AccountsScreen
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Rule
import org.junit.Test

class NavigationTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Test
    fun navigation_fromAccounts_toOperations() {
        composeTestRule.setContent {
            SharedTransitionLayout {
                AnimatedVisibility(visible = true) {
                    TestNavHost(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this,
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("Compte Courant").performClick()
        composeTestRule.onNodeWithText("Operations Screen").assertExists()
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun TestNavHost(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "accounts") {
        composable("accounts") {
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
            AccountsScreen(
                state = state,
                onIntent = {},
                onAccountClick = { navController.navigate("operations") },
                effectFlow = emptyFlow(),
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
        composable("operations") {
            Box {
                Text("Operations Screen")
            }
        }
    }
}
