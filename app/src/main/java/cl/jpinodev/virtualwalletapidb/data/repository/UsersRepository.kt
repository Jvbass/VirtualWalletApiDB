package cl.jpinodev.virtualwalletapidb.data.repository

import cl.jpinodev.virtualwalletapidb.data.model.LoginRequest
import cl.jpinodev.virtualwalletapidb.data.model.LoginTokenResponse
import cl.jpinodev.virtualwalletapidb.data.model.Users
import retrofit2.Response

interface UsersRepository {
    suspend fun createUser(user: Users): Response<Users>

    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginTokenResponse>

}