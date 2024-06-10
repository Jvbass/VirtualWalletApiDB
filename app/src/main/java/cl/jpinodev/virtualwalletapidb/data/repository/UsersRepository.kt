package cl.jpinodev.virtualwalletapidb.data.repository

import cl.jpinodev.virtualwalletapidb.data.model.Users
import retrofit2.Response

interface UsersRepository {
    suspend fun createUser(user: Users): Response<Users>
}