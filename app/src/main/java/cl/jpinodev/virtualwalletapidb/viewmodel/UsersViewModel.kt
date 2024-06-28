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
    private val _connectedUserLD = MutableLiveData<Result<Response<Users>>>()

    val userLD: LiveData<Result<Response<Users>>> = _userLD
    val loginLD: LiveData<Result<Response<LoginResponse>>> = _loginLD
    val connectedUserLD: LiveData<Result<Response<Users>>> = _connectedUserLD

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val loginFromApi = try {
                    usersUseCase.loginUser(LoginRequest(email, password))
                } catch (e: Exception) {
                    null
                }
                if (loginFromApi != null && loginFromApi.isSuccessful) {
                    _loginLD.postValue(Result.success(loginFromApi))
                } else {
                    val loginFromDb = usersUseCase.loginUserFromDb(email, password)
                    if (loginFromDb.isSuccessful) {
                        _loginLD.postValue(Result.success(loginFromDb))
                    } else {
                        _loginLD.postValue(Result.failure(Exception("Error al obtener usuario")))
                    }
                }
            } catch (e: Exception) {
                _loginLD.postValue(Result.failure(e))
            }
        }
    }

    // metodo para crear un usuario
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

    fun getConnectedUser(token: String) {
        viewModelScope.launch {
            try {
                val response = usersUseCase.getConnectedUser("Bearer $token")
                if (response.isSuccessful) {
                    _connectedUserLD.postValue(Result.success(response))
                    usersUseCase.saveUserOnDb(response.body()!!)
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
}
