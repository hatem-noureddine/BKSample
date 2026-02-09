package com.hatem.noureddine.bank.data.repository

import androidx.datastore.preferences.core.preferencesOf
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hatem.noureddine.bank.data.datasource.BankDataSource
import com.hatem.noureddine.bank.data.dto.AccountDto
import com.hatem.noureddine.bank.data.dto.BankDto
import com.hatem.noureddine.bank.data.dto.OperationDto
import com.hatem.noureddine.bank.data.local.dao.BankDao
import com.hatem.noureddine.bank.data.local.entity.AccountEntity
import com.hatem.noureddine.bank.data.local.entity.BankEntity
import com.hatem.noureddine.bank.data.local.entity.OperationEntity
import com.hatem.noureddine.bank.data.local.relation.AccountWithOperations
import com.hatem.noureddine.bank.data.local.relation.BankWithAccounts
import com.hatem.noureddine.bank.domain.model.CABank
import com.hatem.noureddine.bank.test.FakePreferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.collections.List
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class BankRepositoryImplTest {
    private class FakeBankDao : BankDao {
        var banksWithAccountsFlow: Flow<List<BankWithAccounts>> = flowOf(emptyList())
        var accountWithOperationsFlow: Flow<AccountWithOperations?> = flowOf(null)
        var replacedBanks: List<BankEntity> = emptyList()
        var replacedAccounts: List<AccountEntity> = emptyList()
        var replacedOperations: List<OperationEntity> = emptyList()

        override fun getBanksWithAccounts(): Flow<List<BankWithAccounts>> = banksWithAccountsFlow

        override fun getAccountWithOperations(accountId: String): Flow<AccountWithOperations?> = accountWithOperationsFlow

        override suspend fun insertBanks(banks: List<BankEntity>) {}

        override suspend fun insertAccounts(accounts: List<AccountEntity>) {}

        override suspend fun insertOperations(operations: List<OperationEntity>) {}

        override suspend fun clearBanks() {}

        override suspend fun clearAccounts() {}

        override suspend fun clearOperations() {}

        override suspend fun replaceAll(
            banks: List<BankEntity>,
            accounts: List<AccountEntity>,
            operations: List<OperationEntity>,
        ) {
            replacedBanks = banks
            replacedAccounts = accounts
            replacedOperations = operations
        }
    }

    private class FakeBankDataSource : BankDataSource {
        var response: List<BankDto>? = null
        var fetchCount = 0

        override suspend fun fetchBanks(): List<BankDto> {
            fetchCount += 1
            return response ?: error("Response not set")
        }
    }

    @Test
    @Suppress("LongMethod")
    fun `getBanks maps relations to domain models`() =
        runTest {
            val dao = FakeBankDao()
            val dataStore = FakePreferencesDataStore()
            val dataSource = FakeBankDataSource()
            val repository = BankRepositoryImpl(dao, dataStore) { dataSource }

            val accountEntity =
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
            val operationEntity =
                OperationEntity(
                    id = "op1",
                    accountId = "acc1",
                    title = "Op",
                    amount = 9.5,
                    category = "cat",
                    date = 1000L,
                )
            val relation =
                BankWithAccounts(
                    bank = BankEntity(name = "CA", isCA = true),
                    accounts =
                        listOf(
                            AccountWithOperations(
                                account = accountEntity,
                                operations = listOf(operationEntity),
                            ),
                        ),
                )
            dao.banksWithAccountsFlow = flowOf(listOf(relation))

            val banks = repository.getBanks().first()

            assertEquals(1, banks.size)
            assertIs<CABank>(banks.first())
            assertEquals("CA", banks.first().name)
            assertEquals(1, banks.first().accounts.size)
            assertEquals(
                "acc1",
                banks
                    .first()
                    .accounts
                    .first()
                    .id,
            )
            assertEquals(
                1,
                banks
                    .first()
                    .accounts
                    .first()
                    .operations.size,
            )
        }

    @Test
    fun `getAccount maps relation to domain model`() =
        runTest {
            val dao = FakeBankDao()
            val dataStore = FakePreferencesDataStore()
            val dataSource = FakeBankDataSource()
            val repository = BankRepositoryImpl(dao, dataStore) { dataSource }

            val accountEntity =
                AccountEntity(
                    id = "acc1",
                    bankName = "Other",
                    order = 2,
                    holder = "Holder",
                    role = 1,
                    contractNumber = "456",
                    label = "Label 2",
                    productCode = "CODE2",
                    balance = 50.0,
                )
            val relation =
                AccountWithOperations(
                    account = accountEntity,
                    operations = emptyList(),
                )
            dao.accountWithOperationsFlow = flowOf(relation)

            val account = repository.getAccount("acc1").first()

            assertNotNull(account)
            assertEquals("acc1", account.id)
            assertEquals(0, account.operations.size)
        }

    @Test
    fun `getLastSyncTime returns null for missing or invalid values`() =
        runTest {
            val dao = FakeBankDao()
            val dataStore = FakePreferencesDataStore()
            val dataSource = FakeBankDataSource()
            val repository = BankRepositoryImpl(dao, dataStore) { dataSource }

            assertEquals(null, repository.getLastSyncTime().first())

            val key = stringPreferencesKey("last_sync_time")
            val badStore = FakePreferencesDataStore(preferencesOf(key to "not-a-number"))
            val repositoryWithBadStore = BankRepositoryImpl(dao, badStore) { dataSource }

            assertEquals(null, repositoryWithBadStore.getLastSyncTime().first())
        }

    @Test
    fun `syncData forces refresh and persists entities`() =
        runTest {
            val dao = FakeBankDao()
            val dataStore = FakePreferencesDataStore()
            val dataSource = FakeBankDataSource()
            val repository = BankRepositoryImpl(dao, dataStore) { dataSource }

            val response =
                listOf(
                    BankDto(
                        name = "Other Bank",
                        isCA = 0,
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
                                    balance = 100.0,
                                    operations =
                                        listOf(
                                            OperationDto(
                                                id = "op1",
                                                title = "Title",
                                                amount = "10.50",
                                                category = "cat",
                                                date = "1000",
                                            ),
                                        ),
                                ),
                            ),
                    ),
                )
            dataSource.response = response

            repository.syncData(forceRefresh = true)

            assertEquals(1, dataSource.fetchCount)
            assertEquals(1, dao.replacedBanks.size)
            assertEquals(1, dao.replacedAccounts.size)
            assertEquals(1, dao.replacedOperations.size)
        }

    @Test
    fun `syncData skips refresh when within throttle window`() =
        runTest {
            val dao = FakeBankDao()
            val key = stringPreferencesKey("last_sync_time")
            val now = Clock.System.now().toEpochMilliseconds()
            val dataStore = FakePreferencesDataStore(preferencesOf(key to now.toString()))
            val dataSource = FakeBankDataSource()
            val repository = BankRepositoryImpl(dao, dataStore) { dataSource }

            dataSource.response = emptyList()
            repository.syncData(forceRefresh = false)

            assertEquals(0, dataSource.fetchCount)
            assertTrue(dao.replacedBanks.isEmpty())
        }
}
