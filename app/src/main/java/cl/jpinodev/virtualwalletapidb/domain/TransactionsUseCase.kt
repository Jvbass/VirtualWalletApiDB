package cl.jpinodev.virtualwalletapidb.domain

import cl.jpinodev.virtualwalletapidb.data.model.apientities.TransactionResponse
import cl.jpinodev.virtualwalletapidb.data.repository.TransactionsRepository
import retrofit2.Response

class TransactionsUseCase(private val repository: TransactionsRepository) {
    suspend fun getTransactions(token: String): Response<TransactionResponse> {
        return repository.getTransactions(token)
    }
}

/*    suspend fun getOwnAccounts(token: String): Response<List<Accounts>> {
        return accountRepository.getOwnAccounts(token)
    }*/