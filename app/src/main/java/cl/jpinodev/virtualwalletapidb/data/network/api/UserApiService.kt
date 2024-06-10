package cl.jpinodev.virtualwalletapidb.data.network.api

import cl.jpinodev.virtualwalletapidb.data.model.LoginRequest
import cl.jpinodev.virtualwalletapidb.data.model.LoginTokenResponse
import cl.jpinodev.virtualwalletapidb.data.model.Users
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {
    // Endpoint para crear un nuevo usuario, envia por el body el usuario
    // :Response indica que la respuesta es un objeto de tipo Response
    @POST("users")
    suspend fun createUser(@Body user: Users): Response<Users>

    // Endpoint Logear Un usuairo, envia por el body el usuario
    @POST("auth/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginTokenResponse>
}
