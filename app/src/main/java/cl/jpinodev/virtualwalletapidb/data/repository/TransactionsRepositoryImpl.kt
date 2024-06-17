package cl.jpinodev.virtualwalletapidb.data.repository

import cl.jpinodev.virtualwalletapidb.data.model.apientities.TransactionResponse
import cl.jpinodev.virtualwalletapidb.data.network.api.TransactionApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class TransactionsRepositoryImpl(private val transactionService: TransactionApiService) :
    TransactionsRepository {
    override suspend fun getTransactions(token: String): Response<TransactionResponse> {
        return withContext(Dispatchers.IO) {
            transactionService.getTransactions(token)
        }
    }
}
