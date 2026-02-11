package com.hatem.noureddine.bank.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
 * Displays a circular icon with the initial of the account label.
 *
 * @param label The account label (e.g., "Compte Courant").
 */
@Composable
fun AccountIcon(label: String) {
    Box(
        modifier =
            Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        colors =
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.tertiary,
                            ),
                    ),
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label.take(1).uppercase(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
        )
    }
}

/**
 * Displays the account name and details (product code, contract number).
 *
 * @param account The [Account] to display information for.
 */
@Composable
fun AccountInfo(account: Account) {
    Column {
        Text(
            text = account.label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${account.productCode} • ${account.contractNumber}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/**
 * Displays the account balance with color coding (Green for positive, Red for negative).
 *
 * @param balance The balance amount.
 */
@Composable
fun AccountBalance(balance: Double) {
    Column(
        horizontalAlignment = Alignment.End,
    ) {
        // Format currency properly
        Text(
            text = "$balance €",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = if (balance >= 0) ColorPositiveBalance else ColorNegativeBalance,
        )
    }
}

@Preview
@Composable
fun AccountIconPreview() {
    AccountIcon(label = "Compte Courant")
}

@Preview
@Composable
fun AccountInfoPreview() {
    AccountInfo(
        account =
            Account(
                id = "1",
                label = "Compte Courant",
                balance = 1234.56,
                holder = "Holder",
                role = 1,
                order = 1,
                contractNumber = "12345",
                productCode = "CODE",
                operations = emptyList(),
            ),
    )
}

@Preview
@Composable
fun AccountBalancePositivePreview() {
    AccountBalance(balance = 1500.00)
}

@Preview
@Composable
fun AccountBalanceNegativePreview() {
    AccountBalance(balance = -450.50)
}
