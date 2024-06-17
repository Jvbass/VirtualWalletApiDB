package cl.jpinodev.virtualwalletapidb.data.network.api

import cl.jpinodev.virtualwalletapidb.data.model.apientities.SendMoneyRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.SendMoneyResponse
import cl.jpinodev.virtualwalletapidb.data.model.entities.Accounts
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountApiService {
        //obtener las cuentas del usuario
        @GET("accounts/me")
        suspend fun getOwnAccounts(@Header("Authorization") token: String)
        : Response<List<Accounts>>

        //crear una cuenta
        @POST("accounts")
        suspend fun createAccount(@Header("Authorization") token: String,@Body account: Accounts)
        : Response<Accounts>

        @POST("accounts/{id}")
        suspend fun sendDepositMoney(
                @Path("id") accountId: Int,
                @Header("Authorization") token: String,
                @Body request: SendMoneyRequest
        ): Response<SendMoneyResponse>
}
