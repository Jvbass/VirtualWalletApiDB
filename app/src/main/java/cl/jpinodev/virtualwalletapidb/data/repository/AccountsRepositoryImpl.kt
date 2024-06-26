package cl.jpinodev.virtualwalletapidb.data.repository

import cl.jpinodev.virtualwalletapidb.data.local.dao.AccountDao
import cl.jpinodev.virtualwalletapidb.data.model.apientities.AccountRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationResponse
import cl.jpinodev.virtualwalletapidb.data.model.entities.Accounts
import cl.jpinodev.virtualwalletapidb.data.network.api.AccountApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class AccountsRepositoryImpl(
    private val accountService: AccountApiService,
    private val accountDao: AccountDao
) :
    AccountsRepository {
    override suspend fun getOwnAccounts(token: String): Response<List<Accounts>> {
        return withContext(Dispatchers.IO) {
            accountService.getOwnAccounts(token)
        }
    }

    override suspend fun createAccount(token: String, account: AccountRequest): Response<Accounts> {
        return withContext(Dispatchers.IO) {
            accountService.createAccount(token, account)
        }
    }

    override suspend fun sendDepositMoney(
        accountId: Int,
        token: String,
        request: OperationRequest
    ): Response<OperationResponse> {
        return withContext(Dispatchers.IO) {
            accountService.sendDepositMoney(accountId, token, request)
        }
    }

    /*
    * Implementacion metodos DAOs
    * */
    override suspend fun saveAccountOnDb(account: Accounts) {
        return withContext(Dispatchers.IO) {
            accountDao.insertAccount(account)
        }
    }

    override suspend fun getAccountsByUserIdFromDb(userId: Int): Result<List<Accounts>> {
        return withContext(Dispatchers.IO) {
            try {
                val accounts = accountDao.getAccountsByUserId(userId)
                Result.success(accounts)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
