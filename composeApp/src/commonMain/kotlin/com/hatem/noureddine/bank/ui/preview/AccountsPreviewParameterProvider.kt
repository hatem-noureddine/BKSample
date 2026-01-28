package com.hatem.noureddine.bank.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.domain.model.BankSections
import com.hatem.noureddine.bank.domain.model.CABank
import com.hatem.noureddine.bank.domain.model.OtherBank
import com.hatem.noureddine.bank.presentation.viewmodel.accounts.AccountsViewModel

class AccountsPreviewParameterProvider : PreviewParameterProvider<AccountsViewModel.State> {
    override val values: Sequence<AccountsViewModel.State> = sequenceOf(
        // Loading State
        AccountsViewModel.State(isLoading = true),

        // Empty State
        AccountsViewModel.State(
            isLoading = false,
            sections = BankSections(emptyList(), emptyList())
        ),

        // Error State
        AccountsViewModel.State(
            isLoading = false,
            error = "Network Error"
        ),

        // Content State with CA Banks
        AccountsViewModel.State(
            isLoading = false,
            sections = BankSections(
                caBanks = listOf(
                    CABank(
                        name = "Credit Agricole Centre France",
                        accounts = listOf(
                            Account(
                                id = "1",
                                order = 1,
                                holder = "M Hatem Noureddine",
                                role = 1,
                                contractNumber = "123456789",
                                label = "Compte Courant",
                                productCode = "Code",
                                balance = 1234.56,
                                operations = emptyList()
                            ),
                            Account(
                                id = "2",
                                order = 2,
                                holder = "M Hatem Noureddine",
                                role = 1,
                                contractNumber = "987654321",
                                label = "Livret A",
                                productCode = "Code",
                                balance = 10000.00,
                                operations = emptyList()
                            )
                        )
                    )
                ),
                otherBanks = emptyList()
            ),
            expandedBankNames = setOf("Credit Agricole Centre France")
        ),
        
        // Content State with Mixed Banks
         AccountsViewModel.State(
            isLoading = false,
            sections = BankSections(
                caBanks = listOf(
                    CABank(
                        name = "Credit Agricole Centre France",
                        accounts = listOf(
                            Account(
                                id = "1",
                                order = 1,
                                holder = "M Hatem Noureddine",
                                role = 1,
                                contractNumber = "123456789",
                                label = "Compte Courant",
                                productCode = "Code",
                                balance = 1234.56,
                                operations = emptyList()
                            )
                        )
                    )
                ),
                otherBanks = listOf(
                    OtherBank(
                         name = "Boursorama",
                         accounts = listOf(
                             Account(
                                id = "3",
                                order = 3,
                                holder = "M Hatem Noureddine",
                                role = 1,
                                contractNumber = "555666777",
                                label = "Compte Boursorama",
                                productCode = "Code",
                                balance = 500.00,
                                operations = emptyList()
                            )
                         )
                    )
                )
            ),
            expandedBankNames = setOf("Credit Agricole Centre France", "Boursorama")
        )
    )
}
