package cl.jpinodev.virtualwalletapidb.domain

import cl.jpinodev.virtualwalletapidb.data.model.apientities.LoginRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.LoginResponse
import cl.jpinodev.virtualwalletapidb.data.model.entities.Users
import cl.jpinodev.virtualwalletapidb.data.repository.UsersRepository
import retrofit2.Response

// recibe el repositorio
class UsersUseCase(private val repository: UsersRepository) {
    suspend fun createUser(user: Users): Response<Users> {
        return repository.createUser(user)
    }

    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse> {
        return repository.loginUser(loginRequest)
    }

    suspend fun getConnectedUser(token: String): Response<Users> {
        return repository.getConnectedUser(token)
    }

    /*use case para la DB*/
    suspend fun saveUserOnDb(user: Users) {
        return repository.saveUserOnDb(user)
    }

    suspend fun getUserByIdFromDb(id: Int): Users? {
        return repository.getUserByIdFromDb(id)
    }

    suspend fun loginUserFromDb(email: String, password: String): Response<LoginResponse>{
        return repository.loginUserFromDb(email, password)
    }
}