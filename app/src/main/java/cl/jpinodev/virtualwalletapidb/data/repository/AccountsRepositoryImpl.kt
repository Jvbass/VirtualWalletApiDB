package cl.jpinodev.virtualwalletapidb.data.repository

import android.accounts.Account
import cl.jpinodev.virtualwalletapidb.data.model.apientities.SendMoneyRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.SendMoneyResponse
import cl.jpinodev.virtualwalletapidb.data.model.entities.Accounts
import cl.jpinodev.virtualwalletapidb.data.network.api.AccountApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class AccountsRepositoryImpl(private val accountService: AccountApiService):
    AccountsRepository {
    override suspend fun getOwnAccounts(token: String): Response<List<Accounts>> {
        return withContext(Dispatchers.IO) {
            accountService.getOwnAccounts(token)
        }
    }

    override suspend fun createAccount(token: String, account: Accounts): Response<Accounts> {
        return withContext(Dispatchers.IO) {
            accountService.createAccount(token,account)
        }
    }

    override suspend fun sendDepositMoney(
        accountId: Int,
        token: String,
        request: SendMoneyRequest
    ): Response<SendMoneyResponse> {
        return withContext(Dispatchers.IO) {
            accountService.sendDepositMoney(accountId,token,request)
        }
    }
}
