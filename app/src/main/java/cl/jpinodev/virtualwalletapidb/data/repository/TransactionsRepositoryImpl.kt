package cl.jpinodev.virtualwalletapidb.data.repository

import cl.jpinodev.virtualwalletapidb.data.local.dao.TransactionDao
import cl.jpinodev.virtualwalletapidb.data.model.apientities.TransactionResponse
import cl.jpinodev.virtualwalletapidb.data.model.entities.Transactions
import cl.jpinodev.virtualwalletapidb.data.network.api.TransactionApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class TransactionsRepositoryImpl(
    private val transactionService: TransactionApiService,
    private val transactionDao: TransactionDao
) :
    TransactionsRepository {
    override suspend fun getTransactions(token: String): Response<TransactionResponse> {
        return withContext(Dispatchers.IO) {
            transactionService.getTransactions(token)
        }
    }

    /*Implementacion daos*/
    override suspend fun saveTransactionsOnDb(transaction: Transactions) {
        return withContext(Dispatchers.IO) {
            transactionDao.insertTransaction(transaction)
        }
    }

    override suspend fun getTransactionsByAccountIdFromDb(accountId: Int): Result<List<Transactions>> {
        return withContext(Dispatchers.IO) {
            try {
                val transactionsFromDb = transactionDao.getTransactionsByAccountId(accountId)
                Result.success(transactionsFromDb)
            } catch (e: Exception) {
                Result.failure(e)
            }

        }
    }
}
