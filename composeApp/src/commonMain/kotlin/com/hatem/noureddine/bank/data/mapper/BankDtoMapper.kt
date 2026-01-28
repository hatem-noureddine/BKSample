package com.hatem.noureddine.bank.data.mapper

import com.hatem.noureddine.bank.data.dto.BankDto
import com.hatem.noureddine.bank.data.local.entity.AccountEntity
import com.hatem.noureddine.bank.data.local.entity.BankEntity
import com.hatem.noureddine.bank.data.local.entity.OperationEntity

/**
 * Mapper object responsible for transforming Data Transfer Objects (DTOs) into Database Entities.
 */
object BankDtoMapper {
    /**
     * Data class holding the mapped entities ready for insertion into the database.
     *
     * @property bankEntities List of [BankEntity] objects.
     * @property accountEntities List of [AccountEntity] objects.
     * @property operationEntities List of [OperationEntity] objects.
     */
    data class MappedData(
        val bankEntities: List<BankEntity>,
        val accountEntities: List<AccountEntity>,
        val operationEntities: List<OperationEntity>,
    )

    /**
     * Maps a list of [BankDto] to a set of database entities.
     * Flattening the hierarchy from the API response into separate entity lists for relational storage.
     *
     * @param bankDtos The list of bank DTOs.
     * @return [MappedData] containing the separated entities.
     */
    fun mapToEntities(bankDtos: List<BankDto>): MappedData {
        val bankEntities = mutableListOf<BankEntity>()
        val accountEntities = mutableListOf<AccountEntity>()
        val operationEntities = mutableListOf<OperationEntity>()

        bankDtos.forEach { bankDto ->
            bankEntities.add(BankEntity(name = bankDto.name, isCA = bankDto.isCA == 1))
            bankDto.accounts.forEach { accountDto ->
                accountEntities.add(
                    AccountEntity(
                        id = accountDto.id,
                        bankName = bankDto.name, // FK
                        order = accountDto.order,
                        holder = accountDto.holder,
                        role = accountDto.role,
                        contractNumber = accountDto.contractNumber,
                        label = accountDto.label,
                        productCode = accountDto.productCode,
                        balance = accountDto.balance,
                    ),
                )
                accountDto.operations.forEach { opDto ->
                    val dateLong = opDto.date.toLongOrNull() ?: 0L
                    val amountDouble = opDto.amount.replace(",", ".").toDoubleOrNull() ?: 0.0
                    operationEntities.add(
                        OperationEntity(
                            id = opDto.id,
                            accountId = accountDto.id, // FK
                            title = opDto.title,
                            amount = amountDouble,
                            category = opDto.category,
                            date = dateLong,
                        ),
                    )
                }
            }
        }
        return MappedData(bankEntities, accountEntities, operationEntities)
    }
}
