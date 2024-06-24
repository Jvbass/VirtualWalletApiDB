package cl.jpinodev.virtualwalletapidb.domain

import cl.jpinodev.virtualwalletapidb.data.model.apientities.AccountRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationResponse
import cl.jpinodev.virtualwalletapidb.data.model.entities.Accounts
import cl.jpinodev.virtualwalletapidb.data.repository.AccountsRepository
import retrofit2.Response

class AccountsUseCase(private val accountRepository: AccountsRepository) {
    suspend fun getOwnAccounts(token: String): Response<List<Accounts>> {
        return accountRepository.getOwnAccounts(token)
    }

    suspend fun createAccount(token: String,account: AccountRequest): Response<Accounts> {
        return accountRepository.createAccount(token,account)
    }

    suspend fun sendDepositMoney(accountId: Int,token: String,account: OperationRequest): Response<OperationResponse> {
        return accountRepository.sendDepositMoney(accountId,token,account)
    }
}
