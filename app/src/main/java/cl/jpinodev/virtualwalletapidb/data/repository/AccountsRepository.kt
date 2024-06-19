package cl.jpinodev.virtualwalletapidb.data.repository

import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationResponse
import cl.jpinodev.virtualwalletapidb.data.model.apientities.AccountsResponse
import retrofit2.Response

interface AccountsRepository {
    suspend fun getOwnAccounts(token: String): Response<List<AccountsResponse>>
    suspend fun createAccount(token: String, account: AccountsResponse): Response<AccountsResponse>

    suspend fun sendDepositMoney(accountId: Int, token: String, request: OperationRequest):
            Response<OperationResponse>
}