package cl.jpinodev.virtualwalletapidb.domain

import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationResponse
import cl.jpinodev.virtualwalletapidb.data.model.apientities.AccountsResponse
import cl.jpinodev.virtualwalletapidb.data.repository.AccountsRepository
import retrofit2.Response

class AccountsUseCase(private val accountRepository: AccountsRepository) {
    suspend fun getOwnAccounts(token: String): Response<List<AccountsResponse>> {
        return accountRepository.getOwnAccounts(token)
    }

    suspend fun createAccount(token: String,account: AccountsResponse): Response<AccountsResponse> {
        return accountRepository.createAccount(token,account)
    }

    suspend fun sendDepositMoney(accountId: Int,token: String,account: OperationRequest): Response<OperationResponse> {
        return accountRepository.sendDepositMoney(accountId,token,account)
    }
}
