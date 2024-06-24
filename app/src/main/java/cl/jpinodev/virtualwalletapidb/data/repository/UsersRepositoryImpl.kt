package cl.jpinodev.virtualwalletapidb.data.repository

import at.favre.lib.crypto.bcrypt.BCrypt
import cl.jpinodev.virtualwalletapidb.data.local.dao.UserDao
import cl.jpinodev.virtualwalletapidb.data.model.apientities.LoginRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.LoginResponse
import cl.jpinodev.virtualwalletapidb.data.model.entities.Users
import cl.jpinodev.virtualwalletapidb.data.network.api.UserApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response

/*
* encapsula la logica de cómo se accede a los datos, ya sea a traves de una red (usando Retrofit)
*  o base de datos (usando Room).
* Esto permite cambiar la implementacion de acceso a datos sin afectar al resto de la app
* */
class UsersRepositoryImpl(private val userService: UserApiService, private val userDao: UserDao) :
    UsersRepository {
    // funcion suspendida en el hilo IO, envia el usuario al servicio
    override suspend fun createUser(user: Users): Response<Users> {
        return withContext(Dispatchers.IO) {
            userService.createUser(user)
        }
    }

    override suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse> {
        return withContext(Dispatchers.IO) {
            userService.loginUser(loginRequest)
        }
    }

    override suspend fun getConnectedUser(token: String): Response<Users> {
        return withContext(Dispatchers.IO) {
            userService.getConnectedUser(token)
        }
    }

    /*
    * Implementacion db
    * */
    override suspend fun saveUserOnDb(user: Users) {
        return withContext(Dispatchers.IO) {
            userDao.insertUser(user)
        }
    }

    override suspend fun getUserByIdFromDb(id: Int): Users? {
        return withContext(Dispatchers.IO) {
            userDao.getUserById(id)
        }
    }

    override suspend fun loginUserFromDb(email: String, password: String): Response<LoginResponse> {
        return withContext(Dispatchers.IO) {
            val user = userDao.getUserByEmail(email)
            user?.let {
                val result = BCrypt.verifyer().verify(password.toCharArray(), it.password)
                if (result.verified) {
                    val loginResponse = LoginResponse("FakeTokenOffline")
                    Response.success(loginResponse)
                } else {
                    Response.error(
                        401, ResponseBody.create(MediaType.parse("text/plain"), "Invalid credentials")
                    )
                }
            } ?: Response.error(
                401, ResponseBody.create(MediaType.parse("text/plain"), "User not found")
            )
        }
    }
}