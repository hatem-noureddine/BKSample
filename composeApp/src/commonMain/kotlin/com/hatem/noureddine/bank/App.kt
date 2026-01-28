package com.hatem.noureddine.bank

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.hatem.noureddine.bank.presentation.viewmodel.accounts.AccountsViewModel
import com.hatem.noureddine.bank.presentation.viewmodel.main.MainViewModel
import com.hatem.noureddine.bank.presentation.viewmodel.settings.SettingsViewModel
import com.hatem.noureddine.bank.ui.components.BottomNavigationBar
import com.hatem.noureddine.bank.ui.navigation.Route
import com.hatem.noureddine.bank.ui.screens.AccountsScreen
import com.hatem.noureddine.bank.ui.screens.OperationsScreen
import com.hatem.noureddine.bank.ui.screens.SettingsScreen
import com.hatem.noureddine.bank.ui.theme.BankTheme
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun App() {
    BankTheme {
        val viewModel = koinViewModel<MainViewModel>()
        val state by viewModel.uiState.collectAsStateWithLifecycle()
        val appMode = state.appMode

        // Wait for appMode to be loaded (not null)
        if (appMode == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // "Clean up": Force recreation of the entire UI logic when mode changes
            // using key(appMode) ensures ViewModels are cleared and recreated with new dependencies.
            key(appMode) {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) },
                ) { innerPadding ->
                    SharedTransitionLayout(
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Route.Accounts,
                        ) {
                            composable<Route.Accounts> {
                                val viewModel = koinViewModel<AccountsViewModel>()
                                val state by viewModel.uiState.collectAsStateWithLifecycle()

                                AccountsScreen(
                                    state = state,
                                    onIntent = viewModel::handleIntent,
                                    onAccountClick = { accountId ->
                                        navController.navigate(Route.Operations(accountId))
                                    },
                                    effectFlow = viewModel.effect,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@composable,
                                )
                            }

                            composable<Route.Operations> { backStackEntry ->
                                val route: Route.Operations = backStackEntry.toRoute()
                                OperationsScreen(
                                    accountId = route.accountId,
                                    onBackClick = { navController.popBackStack() },
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@composable,
                                )
                            }

                            composable<Route.Settings> {
                                val viewModel = koinViewModel<SettingsViewModel>()
                                val state by viewModel.uiState.collectAsStateWithLifecycle()

                                SettingsScreen(
                                    state = state,
                                    onIntent = viewModel::handleIntent,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
