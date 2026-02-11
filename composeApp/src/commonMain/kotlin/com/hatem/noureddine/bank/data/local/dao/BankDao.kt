package com.hatem.noureddine.bank.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.hatem.noureddine.bank.data.local.entity.AccountEntity
import com.hatem.noureddine.bank.data.local.entity.BankEntity
import com.hatem.noureddine.bank.data.local.entity.OperationEntity
import com.hatem.noureddine.bank.data.local.relation.AccountWithOperations
import com.hatem.noureddine.bank.data.local.relation.BankWithAccounts
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for accessing Bank-related data in the database.
 */
@Dao
interface BankDao {
    /**
     * Retrieves all banks with their associated accounts.
     * @return Flow containing list of [BankWithAccounts].
     */
    @Transaction
    @Query("SELECT * FROM banks")
    fun getBanksWithAccounts(): Flow<List<BankWithAccounts>>

    /**
     * Retrieves a specific account with its operations.
     */
    @Transaction
    @Query("SELECT * FROM accounts WHERE id = :accountId")
    fun getAccountWithOperations(accountId: String): Flow<AccountWithOperations?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanks(banks: List<BankEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccounts(accounts: List<AccountEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOperations(operations: List<OperationEntity>)

    @Query("DELETE FROM banks")
    suspend fun clearBanks()

    @Query("DELETE FROM accounts")
    suspend fun clearAccounts()

    @Query("DELETE FROM operations")
    suspend fun clearOperations()

    /**
     * Transactionally replaces the entire database content with new data.
     * Clears all tables before inserting new entities to ensure a fresh state.
     */
    @Transaction
    suspend fun replaceAll(
        banks: List<BankEntity>,
        accounts: List<AccountEntity>,
        operations: List<OperationEntity>,
    ) {
        clearOperations()
        clearAccounts()
        clearBanks()
        insertBanks(banks)
        insertAccounts(accounts)
        insertOperations(operations)
    }
}
