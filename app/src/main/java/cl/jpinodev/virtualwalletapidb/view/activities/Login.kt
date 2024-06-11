package cl.jpinodev.virtualwalletapidb.view.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import cl.jpinodev.virtualwalletapidb.data.appdata.SharedPreferences
import cl.jpinodev.virtualwalletapidb.data.network.api.UserApiService
import cl.jpinodev.virtualwalletapidb.data.network.retrofit.RetrofitHelper
import cl.jpinodev.virtualwalletapidb.data.repository.UsersRepositoryImpl
import cl.jpinodev.virtualwalletapidb.databinding.ActivityLoginBinding
import cl.jpinodev.virtualwalletapidb.domain.UsersUseCase
import cl.jpinodev.virtualwalletapidb.view.utils.ToastUtils
import cl.jpinodev.virtualwalletapidb.viewmodel.UsersViewModel
import cl.jpinodev.virtualwalletapidb.viewmodel.UsersViewModelFactory

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // instancia de Retrofit que está configurada para comunicarse con la API.
        val userApiService: UserApiService =
            RetrofitHelper.getRetrofit().create(UserApiService::class.java)
        //instancia de UsersRepositoryImpl
        val usersRepository: UsersRepositoryImpl = UsersRepositoryImpl(userApiService)
        //instancia de UsersUseCase le pasamos el repositorio (usersRepository).
        val usersUseCase: UsersUseCase = UsersUseCase(usersRepository)
        //instancia de UsersViewModel usando fabrica UsersViewModelFactory
        val usersViewModel: UsersViewModel by viewModels { UsersViewModelFactory(usersUseCase) }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                ToastUtils.showCustomToast(this, "Todos los campos son obligatorios")
            } else {
                usersViewModel.loginUser(email, password)
            }
        }

        usersViewModel.loginLiveData.observe(this, Observer { result ->
            result.onSuccess { response ->
                val accessToken = response.body()?.accessToken.toString()
                SharedPreferences.saveToken(this, accessToken)
                Log.d("Login", "Token: $accessToken")
                ToastUtils.showCustomToast(this, "Login exitoso")
            }
            result.onFailure {
                ToastUtils.showCustomToast(this, "Error al iniciar sesión: ${it.message}")
            }
        })
    }
}
