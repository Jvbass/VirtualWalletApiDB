package cl.jpinodev.virtualwalletapidb.data.repository

import cl.jpinodev.virtualwalletapidb.data.model.apientities.LoginRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.LoginResponse
import cl.jpinodev.virtualwalletapidb.data.model.entities.Users
import retrofit2.Response

interface UsersRepository {
    suspend fun createUser(user: Users): Response<Users>

    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse>

    suspend fun getConnectedUser(token: String): Response<Users>

}