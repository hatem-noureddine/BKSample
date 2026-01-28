package com.hatem.noureddine.bank.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hatem.noureddine.bank.presentation.viewmodel.operations.OperationsViewModel

/**
 * Header component for the Operations screen.
 * Connects via Shared Element Transition to the selected account item.
 * Contains the TopBar and Balance display.
 *
 * @param state The current UI state.
 * @param onBackClick Callback when the back button is clicked.
 * @param sharedTransitionScope Scope for shared element transitions.
 * @param animatedVisibilityScope Scope for animated visibility.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun OperationsHeader(
    state: OperationsViewModel.State,
    onBackClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    with(sharedTransitionScope) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .sharedElement(
                        rememberSharedContentState(key = "account-${state.account?.id ?: "unknown"}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ).clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                    .background(
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.primary,
                                ),
                        ),
                    ).padding(bottom = 32.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().statusBarsPadding(),
            ) {
                // Top Bar Area
                OperationsTopBar(state, onBackClick)

                // Balance
                OperationsBalance(state.account)
            }
        }
    }
}
