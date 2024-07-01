package cl.jpinodev.virtualwalletapidb.domain

import cl.jpinodev.virtualwalletapidb.data.model.apientities.TransactionResponse
import cl.jpinodev.virtualwalletapidb.data.model.entities.Transactions
import cl.jpinodev.virtualwalletapidb.data.repository.TransactionsRepository
import retrofit2.Response

class TransactionsUseCase(private val transactionRepository: TransactionsRepository) {
    suspend fun getTransactions(token: String): Response<TransactionResponse> {
        return transactionRepository.getTransactions(token)
    }

    /*
    * Use case para db
    * */

    suspend fun saveTransactionsOnDb(transactions: List<Transactions>) {
        transactions.forEach { transaction ->
            transactionRepository.saveTransactionsOnDb(transaction)
        }
    }

    suspend fun getTransactionsFromDb(accountId : Int): Result<List<Transactions>> {
        return transactionRepository.getTransactionsByAccountIdFromDb(accountId)
    }
}
