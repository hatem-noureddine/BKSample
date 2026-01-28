package com.hatem.noureddine.bank.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.hatem.noureddine.bank.data.local.entity.AccountEntity
import com.hatem.noureddine.bank.data.local.entity.BankEntity

data class BankWithAccounts(
    @Embedded val bank: BankEntity,
    @Relation(
        entity = AccountEntity::class,
        parentColumn = "name",
        entityColumn = "bankName",
    )
    val accounts: List<AccountWithOperations>,
)
