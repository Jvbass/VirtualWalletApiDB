package cl.jpinodev.virtualwalletapidb.data.repository

import cl.jpinodev.virtualwalletapidb.data.model.entities.Accounts
import retrofit2.Response

interface AccountsRepository {
    suspend fun getOwnAccounts(token: String): Response<List<Accounts>>
    suspend fun createAccount(token: String, account: Accounts): Response<Accounts>
}