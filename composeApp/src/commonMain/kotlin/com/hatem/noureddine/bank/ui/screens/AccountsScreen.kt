package com.hatem.noureddine.bank.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.hatem.noureddine.bank.presentation.viewmodel.accounts.AccountsViewModel
import com.hatem.noureddine.bank.ui.preview.AccountsPreviewParameterProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow

/**
 * Main screen for displaying the list of accounts.
 * Handles pull-to-refresh, loading states, and navigation to account details.
 *
 * @param state The current UI state from the ViewModel.
 * @param onIntent Callback to send intents to the ViewModel.
 * @param onAccountClick Callback when an account is clicked.
 * @param sharedTransitionScope Scope for shared element transitions.
 * @param animatedVisibilityScope Scope for animated visibility.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AccountsScreen(
    state: AccountsViewModel.State,
    onIntent: (AccountsViewModel.Intent) -> Unit,
    onAccountClick: (String) -> Unit,
    effectFlow: Flow<AccountsViewModel.Effect>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Pull to refresh state
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(effectFlow) {
        effectFlow.collectLatest { effect ->
            when (effect) {
                is AccountsViewModel.Effect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            state = pullToRefreshState,
            onRefresh = { onIntent(AccountsViewModel.Intent.Refresh) },
            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()).fillMaxSize(),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp), // Extra padding for floating bottom bar
            ) {
                // Custom Gradient Header
                item {
                    AccountsHeader()
                }

                // Last Sync Time (Moved below header)
                // Last Sync Time Display
                state.lastUpdateFormatted?.let { formattedTime ->
                    item {
                        Text(
                            text = "Last updated: $formattedTime",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 4.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                        )
                    }
                }

                // CA Banks Section
                bankListSection(
                    title = "VOS COMPTES CRÉDIT AGRICOLE",
                    banks = state.sections.caBanks,
                    expandedBankNames = state.expandedBankNames,
                    onToggle = { onIntent(AccountsViewModel.Intent.ToggleBankSection(it)) },
                    onAccountClick = onAccountClick,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                )

                // Other Banks Section
                bankListSection(
                    title = "VOS COMPTES AUTRES BANQUES",
                    banks = state.sections.otherBanks,
                    expandedBankNames = state.expandedBankNames,
                    onToggle = { onIntent(AccountsViewModel.Intent.ToggleBankSection(it)) },
                    onAccountClick = onAccountClick,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            }

            // Show blocking loader only for initial load (not refreshing)
            if (state.isLoading && !state.isRefreshing) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}



@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun AccountsScreenPreview(
    @PreviewParameter(AccountsPreviewParameterProvider::class) state: AccountsViewModel.State
) {
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
