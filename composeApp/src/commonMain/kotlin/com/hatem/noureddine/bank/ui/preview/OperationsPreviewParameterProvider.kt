package com.hatem.noureddine.bank.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.domain.model.Operation
import com.hatem.noureddine.bank.presentation.viewmodel.operations.OperationsViewModel

class OperationsPreviewParameterProvider : PreviewParameterProvider<OperationsViewModel.State> {
    override val values: Sequence<OperationsViewModel.State> =
        sequenceOf(
            // Loading
            OperationsViewModel.State(isLoading = true),
            // Error
            OperationsViewModel.State(isLoading = false, error = "Failed to load account"),
            // Content
            OperationsViewModel.State(
                isLoading = false,
                account =
                    Account(
                        id = "1",
                        order = 1,
                        holder = "M Hatem Noureddine",
                        role = 1,
                        contractNumber = "123456789",
                        label = "Compte Courant",
                        productCode = "Code",
                        balance = 2500.00,
                        operations = emptyList(),
                    ),
                groupedOperations =
                    mapOf(
                        "Aujourd'hui" to
                            listOf(
                                Operation(
                                    id = "1",
                                    title = "Payment CB",
                                    amount = -50.0,
                                    category = "Food",
                                    date = 1678886400,
                                ),
                                Operation(
                                    id = "2",
                                    title = "Transfer Received",
                                    amount = 1000.0,
                                    category = "Salary",
                                    date = 1678886400,
                                ),
                            ),
                        "Hier" to
                            listOf(
                                Operation(
                                    id = "3",
                                    title = "Netflix",
                                    amount = -15.0,
                                    category = "Subscription",
                                    date = 1678800000,
                                ),
                            ),
                    ),
            ),
        )
}
