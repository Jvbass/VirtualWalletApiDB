package cl.jpinodev.virtualwalletapidb.data.network.api

import cl.jpinodev.virtualwalletapidb.data.model.apientities.TransactionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface TransactionApiService {
    @GET("transactions")
    suspend fun getTransactions(
        @Header("Authorization") token: String
    ): Response<TransactionResponse>
}