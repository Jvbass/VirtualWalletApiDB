package cl.jpinodev.virtualwalletapidb.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.jpinodev.virtualwalletapidb.data.model.apientities.LoginRequest
import cl.jpinodev.virtualwalletapidb.data.model.apientities.LoginResponse
import cl.jpinodev.virtualwalletapidb.data.model.entities.Users
import cl.jpinodev.virtualwalletapidb.domain.UsersUseCase
import kotlinx.coroutines.launch
import retrofit2.Response

class UsersViewModel(private val usersUseCase: UsersUseCase) : ViewModel() {
    private val _userLD = MutableLiveData<Result<Response<Users>>>()
    private val _loginLD = MutableLiveData<Result<Response<LoginResponse>>>()
    private val _connectedUserLD = MutableLiveData<Result<Users?>>()

    val userLD: LiveData<Result<Response<Users>>> = _userLD
    val loginLD: LiveData<Result<Response<LoginResponse>>> = _loginLD
    val connectedUserLD: LiveData<Result<Users?>> = _connectedUserLD

    /*
    * Método Login que asigna a live data _loginLD el resultado de la llamada a la api o la db (token)
    * @params email: String, password: String
    *
    * Response desde la api y db:
    * Success - LoginResponse (token)
    * Failure - Exception (401 - Unauthorized)
    * */
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val loginFromApi = try { //login desde la api
                    usersUseCase.loginUser(LoginRequest(email, password))
                } catch (e: Exception) {
                    null
                }
                if (loginFromApi != null && loginFromApi.isSuccessful) {
                    _loginLD.postValue(Result.success(loginFromApi))
                } else { //login desde la db
                    val loginFromDb = usersUseCase.loginUserFromDb(email, password)
                    if (loginFromDb.isSuccessful) {
                        _loginLD.postValue(Result.success(loginFromDb))
                        val userId = loginFromDb.body()!!.userId
                        getUserByIdFromDb(userId)
                    } else {
                        _loginLD.postValue(Result.failure(Exception("Error al obtener usuario")))
                    }
                }
            } catch (e: Exception) {
                _loginLD.postValue(Result.failure(e))
            }
        }
    }

    /*
    * Metodo para crear un usuario en la api (no usamos db para mantener integridad de datos)
    * Asignamos el resultado de la llamada (un usuario tipo Users) a la api a live data _userLD
    * @params user: Users
    * */
    fun createUser(user: Users) {
        viewModelScope.launch {
            try {
                // llamada al usecase pasamos el usuario recibido desde la vista
                val response = usersUseCase.createUser(user)
                // la api responde un usuario en el request body
                if (response.isSuccessful) {
                    _userLD.postValue(Result.success(response))
                    // usersUseCase.saveUserOnDb(user)
                } else {
                    _userLD.postValue(Result.failure(Exception(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _userLD.postValue(Result.failure(e))
            }
        }
    }

    /*
    * Metodo para obtener desde la api el usuario conectado asigna al live data _connectedUserLD y
    * guardamos el usuario en la db
    * @params token: String
    * */
    fun getConnectedUser(token: String) {
        viewModelScope.launch {
            try {
                val response = usersUseCase.getConnectedUser("Bearer $token")
                if (response.isSuccessful) {
                    val user = response.body()
                    _connectedUserLD.postValue(Result.success(user))
                    user?.let { usersUseCase.saveUserOnDb(it) }
                    Log.i("UsersViewModel", "getConnectedUser: ${response.body()}")
                } else {
                    _connectedUserLD.postValue(
                        Result.failure(
                            Exception(
                                response.errorBody()?.string()
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                _connectedUserLD.postValue(Result.failure(e))
            }
        }
    }

    /*
    * Metodo que busca el usuario en la db por id
    * asigna al livedata connectedUserLD el usuario obtenido que es observado en el homepage
    * @params id: Int
    * */
    fun getUserByIdFromDb(id: Int) {
        viewModelScope.launch {
            val result = usersUseCase.getUserByIdFromDb(id)
            _connectedUserLD.postValue(result)
        }
    }
}
