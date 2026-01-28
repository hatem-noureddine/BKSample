package com.hatem.noureddine.bank.data.mapper

import com.hatem.noureddine.bank.data.local.entity.OperationEntity
import com.hatem.noureddine.bank.data.local.relation.AccountWithOperations
import com.hatem.noureddine.bank.data.local.relation.BankWithAccounts
import com.hatem.noureddine.bank.domain.model.Account
import com.hatem.noureddine.bank.domain.model.Bank
import com.hatem.noureddine.bank.domain.model.CABank
import com.hatem.noureddine.bank.domain.model.Operation
import com.hatem.noureddine.bank.domain.model.OtherBank

/**
 * Extension function to map a [BankWithAccounts] relation to a Domain [Bank] model.
 *
 * @receiver [BankWithAccounts] The database relation object.
 * @return Mapped [Bank] domain model (either [CABank] or [OtherBank]).
 */
fun BankWithAccounts.toDomain(): Bank {
    val domainAccounts = accounts.map { it.toDomain() }
    return if (bank.isCA) {
        CABank(name = bank.name, accounts = domainAccounts)
    } else {
        OtherBank(name = bank.name, accounts = domainAccounts)
    }
}

/**
 * Extension function to map an [AccountWithOperations] relation to a Domain [Account] model.
 *
 * @receiver [AccountWithOperations] The database relation object.
 * @return Mapped [Account] domain model.
 */
fun AccountWithOperations.toDomain(): Account =
    Account(
        id = account.id,
        order = account.order,
        holder = account.holder,
        role = account.role,
        contractNumber = account.contractNumber,
        label = account.label,
        productCode = account.productCode,
        balance = account.balance,
        operations = operations.map { it.toDomain() },
    )

/**
 * Extension function to map an [OperationEntity] to a Domain [Operation] model.
 *
 * @receiver [OperationEntity] The database entity.
 * @return Mapped [Operation] domain model.
 */
fun OperationEntity.toDomain(): Operation =
    Operation(
        id = id,
        title = title,
        amount = amount,
        category = category,
        date = date,
    )
