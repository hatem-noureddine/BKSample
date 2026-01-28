package com.hatem.noureddine.bank.data.mapper

import com.hatem.noureddine.bank.data.dto.AccountDto
import com.hatem.noureddine.bank.data.dto.BankDto
import com.hatem.noureddine.bank.data.dto.OperationDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BankDtoMapperTest {
    @Test
    fun `mapToEntities flattens nested structures`() {
        val response =
            listOf(
                BankDto(
                    name = "CA",
                    isCA = 1,
                    accounts =
                        listOf(
                            AccountDto(
                                id = "acc1",
                                order = 1,
                                holder = "Holder",
                                role = 1,
                                contractNumber = "123",
                                label = "Label",
                                productCode = "CODE",
                                balance = 10.0,
                                operations =
                                    listOf(
                                        OperationDto(
                                            id = "op1",
                                            title = "Title",
                                            amount = "10,50",
                                            category = "cat",
                                            date = "1000",
                                        ),
                                    ),
                            ),
                        ),
                ),
            )

        val mapped = BankDtoMapper.mapToEntities(response)

        assertEquals(1, mapped.bankEntities.size)
        assertEquals(1, mapped.accountEntities.size)
        assertEquals(1, mapped.operationEntities.size)
        assertEquals("CA", mapped.bankEntities.first().name)
        assertEquals("acc1", mapped.accountEntities.first().id)
        assertEquals(10.50, mapped.operationEntities.first().amount)
    }

    @Test
    fun `mapToEntities handles invalid numeric values`() {
        val response =
            listOf(
                BankDto(
                    name = "Other",
                    isCA = 0,
                    accounts =
                        listOf(
                            AccountDto(
                                id = "acc2",
                                order = 1,
                                holder = "Holder",
                                role = 1,
                                contractNumber = "123",
                                label = "Label",
                                productCode = "CODE",
                                balance = 10.0,
                                operations =
                                    listOf(
                                        OperationDto(
                                            id = "op2",
                                            title = "Title",
                                            amount = "not-a-number",
                                            category = "cat",
                                            date = "not-a-date",
                                        ),
                                    ),
                            ),
                        ),
                ),
            )

        val mapped = BankDtoMapper.mapToEntities(response)

        assertEquals(0.0, mapped.operationEntities.first().amount)
        assertEquals(0L, mapped.operationEntities.first().date)
        assertTrue(
            mapped.bankEntities
                .first()
                .isCA
                .not(),
        )
    }
}
