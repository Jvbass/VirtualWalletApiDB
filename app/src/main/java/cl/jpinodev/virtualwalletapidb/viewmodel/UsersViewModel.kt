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
    private val _userLiveData = MutableLiveData<Result<Response<Users>>>()
    private val _loginLiveData = MutableLiveData<Result<Response<LoginResponse>>>()

    val userLiveData: LiveData<Result<Response<Users>>> = _userLiveData
    val loginLiveData: LiveData<Result<Response<LoginResponse>>> = _loginLiveData

    // metodo para crear un usuario
    fun createUser(user: Users) {
        viewModelScope.launch {
            try {
                // llamada al usecase pasamos el usuario recibido desde la vista
                // manejamos el response asignandola a _userLiveData
                val response = usersUseCase.createUser(user)
                Log.i("UsersViewModel", "createUser: $response")
                if (response.isSuccessful) {
                    _userLiveData.postValue(Result.success(response))
                } else {
                    _userLiveData.postValue(Result.failure(Exception(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _userLiveData.postValue(Result.failure(e))
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = usersUseCase.loginUser(LoginRequest(email, password))
                if (response.isSuccessful) {
                    _loginLiveData.postValue(Result.success(response))
                } else {
                    _loginLiveData.postValue(Result.failure(Exception(response.errorBody()?.string())))
                }
            } catch (e: Exception) {
                _loginLiveData.postValue(Result.failure(e))
            }
        }
    }
}