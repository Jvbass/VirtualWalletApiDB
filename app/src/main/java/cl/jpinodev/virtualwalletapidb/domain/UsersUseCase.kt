package cl.jpinodev.virtualwalletapidb.domain

import cl.jpinodev.virtualwalletapidb.data.model.apientities.LoginRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.LoginResponse
import cl.jpinodev.virtualwalletapidb.data.model.apientities.UsersResponse
import cl.jpinodev.virtualwalletapidb.data.repository.UsersRepository
import retrofit2.Response

// recibe el repositorio
class UsersUseCase(private val repository: UsersRepository) {
    suspend fun createUser(user: UsersResponse): Response<UsersResponse> {
        return repository.createUser(user)
    }

    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse> {
        return repository.loginUser(loginRequest)
    }

    suspend fun getConnectedUser(token: String): Response<UsersResponse> {
        return repository.getConnectedUser(token)
    }
}