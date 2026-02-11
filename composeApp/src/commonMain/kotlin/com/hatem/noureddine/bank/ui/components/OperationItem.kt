package com.hatem.noureddine.bank.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Receipt
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hatem.noureddine.bank.domain.model.Operation
import com.hatem.noureddine.bank.ui.theme.ColorNegativeBalance
import com.hatem.noureddine.bank.ui.theme.ColorPositiveBalance
import com.hatem.noureddine.bank.ui.util.DateUtils

/**
 * Composable representing a single Operation (Transaction) item.
 *
 * @param operation The [Operation] to display.
 */
@Composable
fun OperationItem(operation: Operation) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Category Icon Placeholder
            Box(
                modifier =
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Receipt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp),
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = operation.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = DateUtils.formatEpoch(operation.date) + " • " + operation.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Text(
                text = "${operation.amount} €",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (operation.amount >= 0) ColorPositiveBalance else ColorNegativeBalance,
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(start = 72.dp), // Indented divider
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
        )
    }
}

@Preview
@Composable
private fun OperationItemPreview() {
    val operation =
        Operation(
            id = "1",
            title = "Carrefour",
            amount = -45.50,
            category = "Shopping",
            date = 1678886400,
        )
    OperationItem(operation = operation)
}
