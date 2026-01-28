package com.hatem.noureddine.bank.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hatem.noureddine.bank.presentation.viewmodel.operations.OperationsViewModel
import com.hatem.noureddine.bank.ui.components.OperationItem
import com.hatem.noureddine.bank.ui.preview.OperationsPreviewParameterProvider
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

/**
 * Main screen for displaying operation details for a specific account.
 * Handles fetching data based on accountId and displaying the list of operations.
 *
 * @param accountId The ID of the account to load.
 * @param onBackClick Callback when the back button is clicked.
 * @param sharedTransitionScope Scope for shared element transitions.
 * @param animatedVisibilityScope Scope for animated visibility.
 */
@OptIn(KoinExperimentalAPI::class, ExperimentalSharedTransitionApi::class)
@Composable
fun OperationsScreen(
    viewModel: OperationsViewModel = koinViewModel(),
    accountId: String,
    onBackClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(accountId) {
        viewModel.handleIntent(OperationsViewModel.Intent.LoadAccount(accountId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is OperationsViewModel.Effect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        OperationsContent(
            state = state,
            onBackClick = onBackClick,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/**
 * Content composable for OperationsScreen.
 * Displays loading, error, or the list of operations grouped by date.
 *
 * @param state The current UI state.
 * @param onBackClick Callback when the back button is clicked.
 * @param sharedTransitionScope Scope for shared element transitions.
 * @param animatedVisibilityScope Scope for animated visibility.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun OperationsContent(
    state: OperationsViewModel.State,
    onBackClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (state.error != null) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                // 1. Custom Immersive Header
                item {
                    with(sharedTransitionScope) {
                        OperationsHeader(
                            state = state,
                            onBackClick = onBackClick,
                            sharedTransitionScope = this@with,
                            animatedVisibilityScope = animatedVisibilityScope,
                        )
                    }
                }

                // 2. Operations List (Grouped)
                if (state.operations.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No operations",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    state.groupedOperations.forEach { (dateHeader, operations) ->
                        item {
                            Text(
                                text = dateHeader.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(start = 24.dp, top = 24.dp, bottom = 8.dp),
                            )
                        }
                        items(operations) { operation ->
                            OperationItem(operation = operation.copy(date = operation.date * 1000))
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(48.dp)) }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun OperationsScreenPreview(
    @PreviewParameter(OperationsPreviewParameterProvider::class) state: OperationsViewModel.State,
) {
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
