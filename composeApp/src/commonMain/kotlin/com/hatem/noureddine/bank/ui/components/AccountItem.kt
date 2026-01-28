package com.hatem.noureddine.bank.ui.components
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.ui.theme.ColorNegativeBalance
import com.hatem.noureddine.bank.ui.theme.ColorPositiveBalance

/**
 * Composable representing a single Account card in the list.
 * Supports Shared Element Transitions.
 *
 * @param account The [Account] to display.
 * @param onClick Callback when the item is clicked.
 * @param sharedTransitionScope Scope for shared element transitions.
 * @param animatedVisibilityScope Scope for animated visibility.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AccountItem(
    account: Account,
    onClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    with(sharedTransitionScope) {
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 16.dp)
                    .sharedElement(
                        rememberSharedContentState(key = "account-${account.id}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ).clickable { onClick() },
            colors =
                CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Increased elevation for pop
            shape = RoundedCornerShape(16.dp),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Left: Icon/Initial + Info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f),
                ) {
                    AccountIcon(label = account.label)

                    Spacer(modifier = Modifier.width(16.dp))

                    AccountInfo(account = account)
                }

                // Right: Balance + Chevron
                AccountBalance(balance = account.balance)
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun AccountItemPreview() {
    val account = Account(
        id = "1",
        order = 1,
        holder = "Holder",
        role = 1,
        contractNumber = "12345",
        label = "Compte Courant",
        productCode = "CODE",
        balance = 1234.56,
        operations = emptyList()
    )
    SharedTransitionLayout {
        AnimatedVisibility(visible = true) {
            AccountItem(
                account = account,
                onClick = {},
                sharedTransitionScope = this@SharedTransitionLayout,
                animatedVisibilityScope = this
            )
        }
    }
}
