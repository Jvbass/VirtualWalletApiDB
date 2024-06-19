package cl.jpinodev.virtualwalletapidb.data.repository

import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationResponse
import cl.jpinodev.virtualwalletapidb.data.model.entities.Accounts
import retrofit2.Response

interface AccountsRepository {
    suspend fun getOwnAccounts(token: String): Response<List<Accounts>>
    suspend fun createAccount(token: String, account: Accounts): Response<Accounts>

    suspend fun sendDepositMoney(accountId: Int, token: String, request: OperationRequest):
            Response<OperationResponse>

    /*implementacion daos*/

    suspend fun saveAccountOnDb(account: Accounts)

    suspend fun getAccountsByUserFromDb(userId: Int): List<Accounts>

}