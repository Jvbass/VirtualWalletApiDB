package cl.jpinodev.virtualwalletapidb.domain

import cl.jpinodev.virtualwalletapidb.data.model.LoginRequest
import cl.jpinodev.virtualwalletapidb.data.model.LoginTokenResponse
import cl.jpinodev.virtualwalletapidb.data.model.Users
import cl.jpinodev.virtualwalletapidb.data.repository.UsersRepository
import retrofit2.Response

// recibe el repositorio
class UsersUseCase(private val repository: UsersRepository) {
    suspend fun createUser(user: Users): Response<Users> {
        return repository.createUser(user)
    }

    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginTokenResponse> {
        return repository.loginUser(loginRequest)
    }
}