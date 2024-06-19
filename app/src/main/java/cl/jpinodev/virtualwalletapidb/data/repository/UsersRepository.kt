package cl.jpinodev.virtualwalletapidb.data.repository

import cl.jpinodev.virtualwalletapidb.data.model.apientities.LoginRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.LoginResponse
import cl.jpinodev.virtualwalletapidb.data.model.apientities.UsersResponse
import retrofit2.Response

interface UsersRepository {
    suspend fun createUser(user: UsersResponse): Response<UsersResponse>

    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse>

    suspend fun getConnectedUser(token: String): Response<UsersResponse>

}