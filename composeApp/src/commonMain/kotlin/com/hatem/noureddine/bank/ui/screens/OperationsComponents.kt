package com.hatem.noureddine.bank.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.presentation.viewmodel.operations.OperationsViewModel

/**
 * Top bar for the Operations screen, displaying the account label and a back button.
 *
 * @param state The current UI state containing account details.
 * @param onBackClick Callback when the back button is clicked.
 */
@Composable
fun OperationsTopBar(
    state: OperationsViewModel.State,
    onBackClick: () -> Unit,
) {
    val title = state.account?.label?.uppercase() ?: "DETAILS"

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 16.dp, start = 8.dp, end = 8.dp),
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onPrimary,
            )
        }

        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
            fontWeight = FontWeight.Bold,
        )
    }
}

/**
 * Displays the current balance of the account on the Operations screen.
 *
 * @param account The [Account] to display the balance for.
 */
@Composable
fun OperationsBalance(account: Account?) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Solde disponible",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${account?.balance} €",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Preview
@Composable
private fun OperationsTopBarPreview() {
    OperationsTopBar(
        state =
            OperationsViewModel.State(
                account =
                    Account(
                        id = "1",
                        label = "Compte Courant",
                        balance = 1234.56,
                        holder = "Test Holder",
                        operations = emptyList(),
                        role = 1,
                        order = 1,
                        contractNumber = "123",
                        productCode = "CODE",
                    ),
            ),
        onBackClick = {},
    )
}

@Preview
@Composable
private fun OperationsBalancePreview() {
    OperationsBalance(
        account =
            Account(
                id = "1",
                label = "Compte Courant",
                balance = 1234.56,
                holder = "Test Holder",
                operations = emptyList(),
                role = 1,
                order = 1,
                contractNumber = "123",
                productCode = "CODE",
            ),
    )
}
