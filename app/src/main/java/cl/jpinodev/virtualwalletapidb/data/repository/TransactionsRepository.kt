package cl.jpinodev.virtualwalletapidb.data.repository

import cl.jpinodev.virtualwalletapidb.data.model.apientities.TransactionResponse
import cl.jpinodev.virtualwalletapidb.data.model.entities.Transactions
import retrofit2.Response

interface TransactionsRepository {
    suspend fun getTransactions(token: String): Response<TransactionResponse>
}