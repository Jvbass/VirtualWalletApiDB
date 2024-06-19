package cl.jpinodev.virtualwalletapidb.data.network.api

import cl.jpinodev.virtualwalletapidb.data.model.apientities.LoginRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.LoginResponse
import cl.jpinodev.virtualwalletapidb.data.model.apientities.UsersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApiService {
    // Endpoint para crear un nuevo usuario, envia por el body el usuario
    // :Response indica que la respuesta es un objeto de tipo Response
    @POST("users")
    suspend fun createUser(@Body user: UsersResponse): Response<UsersResponse>

    // Endpoint Logear Un usuairo, envia por el body el usuario
    @POST("auth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("auth/me")
    suspend fun getConnectedUser(@Header("Authorization") token: String): Response<UsersResponse>
}
