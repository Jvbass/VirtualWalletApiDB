package cl.jpinodev.virtualwalletapidb.data.repository

import cl.jpinodev.virtualwalletapidb.data.model.Users
import cl.jpinodev.virtualwalletapidb.data.network.api.UserApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

/*
* encapsula la logica de cómo se accede a los datos, ya sea a traves de una red (usando Retrofit)
*  o base de datos (usando Room).
* Esto permite cambiar la implementacion de acceso a datos sin afectar al resto de la app
* */
class UsersRepositoryImpl(private val userService: UserApiService): UsersRepository {
    // funcion suspendida en el hilo IO, envia el usuario al servicio
    override suspend fun createUser(user: Users): Response<Users> {
        return withContext(Dispatchers.IO) {
            userService.createUser(user)
        }
    }
}