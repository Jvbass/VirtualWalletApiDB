package cl.jpinodev.virtualwalletapidb.data.network.api

import cl.jpinodev.virtualwalletapidb.data.model.entities.Accounts
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AccountApiService {
        @POST("accounts")
        suspend fun createAccount(@Header("Authorization") token: String,@Body account: Accounts)
        : Response<Accounts>
}
