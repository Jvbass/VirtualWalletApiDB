package cl.jpinodev.virtualwalletapidb.data.network.api

import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.OperationResponse
import cl.jpinodev.virtualwalletapidb.data.model.apientities.AccountsResponse
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
        : Response<List<AccountsResponse>>

        //crear una cuenta
        @POST("accounts")
        suspend fun createAccount(@Header("Authorization") token: String,@Body account: AccountsResponse)
        : Response<AccountsResponse>

        @POST("accounts/{id}")
        suspend fun sendDepositMoney(
                @Path("id") accountId: Int,
                @Header("Authorization") token: String,
                @Body request: OperationRequest
        ): Response<OperationResponse>
}
