package com.hatem.noureddine.bank.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding

import com.hatem.noureddine.bank.domain.model.Bank
import com.hatem.noureddine.bank.ui.components.AccountItem
import com.hatem.noureddine.bank.ui.components.SectionHeader

/**
 * Extension function for LazyListScope to display a section of banks.
 * Includes a title and a list of expandable bank headers with their accounts.
 *
 * @param title The section title (e.g., "VOS COMPTES CRÉDIT AGRICOLE").
 * @param banks List of [Bank] objects to display.
 * @param expandedBankNames Set of currently expanded bank names.
 * @param onToggle Callback to toggle the expansion state of a bank.
 * @param onAccountClick Callback when an account is clicked.
 * @param sharedTransitionScope Scope for shared element transitions.
 * @param animatedVisibilityScope Scope for animated visibility.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Suppress("LongParameterList")
fun LazyListScope.bankListSection(
    title: String,
    banks: List<Bank>,
    expandedBankNames: Set<String>,
    onToggle: (String) -> Unit,
    onAccountClick: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    if (banks.isNotEmpty()) {
        item {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 12.dp),
            )
        }

        banks.forEach { bank ->
            item {
                val isExpanded = expandedBankNames.contains(bank.name)
                SectionHeader(
                    title = bank.name,
                    isExpanded = isExpanded,
                    onToggle = { onToggle(bank.name) },
                )
            }
            if (expandedBankNames.contains(bank.name)) {
                items(bank.accounts) { account ->
                    with(sharedTransitionScope) {
                        AccountItem(
                            account = account,
                            onClick = { onAccountClick(account.id) },
                            sharedTransitionScope = this@with,
                            animatedVisibilityScope = animatedVisibilityScope,
                        )
                    }
                }
            }
        }
    }
}
