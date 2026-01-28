package com.hatem.noureddine.bank.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.hatem.noureddine.bank.data.local.entity.AccountEntity
import com.hatem.noureddine.bank.data.local.entity.OperationEntity

data class AccountWithOperations(
    @Embedded val account: AccountEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "accountId",
    )
    val operations: List<OperationEntity>,
)
