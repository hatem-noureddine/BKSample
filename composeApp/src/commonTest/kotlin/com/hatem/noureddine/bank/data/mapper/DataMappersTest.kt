package com.hatem.noureddine.bank.data.mapper

import com.hatem.noureddine.bank.data.local.entity.AccountEntity
import com.hatem.noureddine.bank.data.local.entity.BankEntity
import com.hatem.noureddine.bank.data.local.entity.OperationEntity
import com.hatem.noureddine.bank.data.local.relation.AccountWithOperations
import com.hatem.noureddine.bank.data.local.relation.BankWithAccounts
import com.hatem.noureddine.bank.domain.model.CABank
import com.hatem.noureddine.bank.domain.model.OtherBank
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DataMappersTest {
    @Test
    fun `BankWithAccounts maps to CABank when isCA true`() {
        val bank = BankEntity(name = "CA", isCA = true)
        val account =
            AccountEntity(
                id = "acc1",
                bankName = "CA",
                order = 1,
                holder = "Holder",
                role = 1,
                contractNumber = "123",
                label = "Label",
                productCode = "CODE",
                balance = 10.0,
            )
        val relation =
            BankWithAccounts(
                bank = bank,
                accounts = listOf(AccountWithOperations(account, emptyList())),
            )

        val mapped = relation.toDomain()

        assertIs<CABank>(mapped)
        assertEquals("CA", mapped.name)
        assertEquals(1, mapped.accounts.size)
    }

    @Test
    fun `BankWithAccounts maps to OtherBank when isCA false`() {
        val bank = BankEntity(name = "Other", isCA = false)
        val relation = BankWithAccounts(bank = bank, accounts = emptyList())

        val mapped = relation.toDomain()

        assertIs<OtherBank>(mapped)
        assertEquals("Other", mapped.name)
    }

    @Test
    fun `AccountWithOperations maps to domain Account`() {
        val account =
            AccountEntity(
                id = "acc1",
                bankName = "CA",
                order = 1,
                holder = "Holder",
                role = 1,
                contractNumber = "123",
                label = "Label",
                productCode = "CODE",
                balance = 10.0,
            )
        val operation =
            OperationEntity(
                id = "op1",
                accountId = "acc1",
                title = "Title",
                amount = 5.0,
                category = "cat",
                date = 1000L,
            )
        val relation = AccountWithOperations(account = account, operations = listOf(operation))

        val mapped = relation.toDomain()

        assertEquals("acc1", mapped.id)
        assertEquals(1, mapped.operations.size)
        assertEquals("op1", mapped.operations.first().id)
    }
}
