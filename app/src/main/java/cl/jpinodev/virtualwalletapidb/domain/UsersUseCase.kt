package cl.jpinodev.virtualwalletapidb.domain

import cl.jpinodev.virtualwalletapidb.data.model.apientities.LoginRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.LoginResponse
import cl.jpinodev.virtualwalletapidb.data.model.entities.Users
import cl.jpinodev.virtualwalletapidb.data.repository.UsersRepository
import retrofit2.Response

// recibe el repositorio
class UsersUseCase(private val usersRepository: UsersRepository) {
    suspend fun createUser(user: Users): Response<Users> {
        return usersRepository.createUser(user)
    }

    suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse> {
        return usersRepository.loginUser(loginRequest)
    }

    suspend fun getConnectedUser(token: String): Response<Users> {
        return usersRepository.getConnectedUser(token)
    }

    /*use case para la DB*/
    suspend fun saveUserOnDb(user: Users) {
        return usersRepository.saveUserOnDb(user)
    }

    suspend fun getUserByIdFromDb(id: Int): Result<Users?> {
        return usersRepository.getUserByIdFromDb(id)
    }

    suspend fun loginUserFromDb(email: String, password: String): Response<LoginResponse>{
        return usersRepository.loginUserFromDb(email, password)
    }
}