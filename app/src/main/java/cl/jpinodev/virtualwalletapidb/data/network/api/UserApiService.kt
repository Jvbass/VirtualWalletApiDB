package cl.jpinodev.virtualwalletapidb.data.network.api

import cl.jpinodev.virtualwalletapidb.data.model.Users
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {
/*
* Call es una abstracción de una peticion HTTP que encapsula toda la información necesaria para
*  realizar la llamada y manejar la respuesta.
* */

    // Endpoint para crear un nuevo usuario, envia por el body el usuario
    // :Response indica que la respuesta es un objeto de tipo Response
    @POST("users")
    suspend fun createUser(@Body user: Users): Response<Users>
}
